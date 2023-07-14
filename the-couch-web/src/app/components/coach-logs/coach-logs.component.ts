import { Component, OnInit } from '@angular/core';
import { style, animate, transition, trigger } from '@angular/animations';
import { CoachLogsService } from '../../services/coach-logs.service';
import * as XLSX from 'xlsx';
import { HttpResponse } from '@angular/common/http';
// import Swal from 'sweetalert2';

@Component({
  selector: 'app-coach-logs',
  templateUrl: './coach-logs.component.html',
  styleUrls: ['./coach-logs.component.css'],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        // :enter is alias to 'void => *'
        style({ opacity: 0 }),
        animate(600, style({ opacity: 1 })),
      ]),
    ]),
  ],
})
export class CoachLogsComponent implements OnInit {

  coachingLog = {
      noInGroup: '',
      clientName: '',
      clientEmail: '',
      startDate: '',
      endDate: '',
      paidHours: '',
      proBonoHours: '',
      createdAt: '',
      createdBy: ''
  };
  loading = false;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };
  coachingLogs: any;
  userRole: any;
  page: number = 0;
  pageSize: number = 20;
  totalElements: any;
  excelData: any;
  uploadFromExcel: boolean = false;
  headerRow: any  = [];
  coachingLogsFromExcel: any = [];
  showErrorMessage: boolean = false;

  constructor(private coachLogsService: CoachLogsService) { }

  ngOnInit(): void {

    this.getCoachingLogs(0);
  }

  getCoachingLogs(page: any) {
    this.loading = true;
    const options = {
      page: page,
      sort: 'id,desc',
      size: this.itemsPerPage,
      search: this.filters.searchItem,
    };
    this.coachLogsService.getCoachLogs(options).subscribe((response: any) => {
      this.coachingLogs = response.body;
      this.totalElements = +response.headers.get('X-Total-Count');
      alert(this.totalElements);
      this.loading = false;
    }, (error: any) => {
      console.log(error);
      this.loading = false;
    }
    );

  }


  onTableDataChange(event: any) {
    this.page = event;
    this.getCoachingLogs(this.page);
  }

  addCoachingLog() {
    console.log(this.coachingLog);
    this.loading = true;
    let log = [this.coachingLog]
    this.coachLogsService.addCoachLogs(log).subscribe({
      next: (response) => {
        console.log(response);
        this.loading = false;
        this.getCoachingLogs(0);
      }
    });

  }

  //upload excel file
  onFileChange(event: any) {
    //read the .xlsx file uploaded and get the data from the file and store it in coachingLogs
    let file = event.target.files[0];
    
    let reader = new FileReader();

    if (file) {
      
      if (reader.readyState === 1) {
        // Abort the previous reading operation if it's still in progress
        reader.abort();
      }

      reader.readAsBinaryString(file);
      reader.onload = (e: any) => {
        let data = new Uint8Array(e.target.result);
        let workbook = XLSX.read(reader.result, { type: 'binary' });
        let sheetNames = workbook.SheetNames;

        //get header row
        this.headerRow = XLSX.utils.sheet_to_json(workbook.Sheets[sheetNames[0]], { header: 1 })[0];
        console.log('Header row', this.headerRow);
        this.excelData = XLSX.utils.sheet_to_json(workbook.Sheets[sheetNames[0]]);
        console.log('Excel data', this.excelData);

        console.log('Header row', this.headerRow);

        console.log('Data', this.coachingLogs);

        
      };

      reader.readAsArrayBuffer(file);


    }
  }


  uploadExcel() {
    this.loading = true;

    //make excelData an array of objects with the same keys as coachingLogs
    this.excelData.forEach((element: any) => {
      let log = {
        clientName: element['Client’s Name'],
        clientEmail: element['Email Address'],
        noInGroup: element['No. in Group'],
        //make sure the date is like 2023-07-14 
        startDate: element['Start Date']?.slice(0, 10).split('/').reverse().join('-'),
        endDate: element['End Date']?.slice(0, 10).split('/').reverse().join('-'),
        paidHours: element['Paid Hours'],
        proBonoHours: element['Pro-bono Hours'],
      };
      this.coachingLogsFromExcel.push(log);
    }
    );

    console.log('Coaching logs', this.coachingLogsFromExcel);

    this.coachLogsService.addCoachLogs(this.coachingLogsFromExcel).subscribe({
      next: (response) => {
        console.log(response);
        this.loading = false;
        this.showErrorMessage = false;
        this.clearAndClose();
        this.getCoachingLogs(0);
      }, error: (error) => {
        console.log(error);
        this.loading = false;
        this.coachingLogsFromExcel = [];
        this.showErrorMessage = true;
      }
    });

  }

  fromExcel() {
    this.uploadFromExcel = !this.uploadFromExcel;
  }

  deleteRow(index: any) {
    this.excelData.splice(index, 1);
  }

  clearData() {
    this.excelData = [];
    //the input file element is not cleared when the data is cleared
    //so we need to clear it manually
    let fileInput = document.getElementById('fileInput') as HTMLInputElement;
    fileInput.value = '';

  }

  clearAndClose() {
    this.clearData();
    this.uploadFromExcel = false;
  }

}
