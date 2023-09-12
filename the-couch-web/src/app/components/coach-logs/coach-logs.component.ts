import { Component, OnInit } from '@angular/core';
import { style, animate, transition, trigger } from '@angular/animations';
import { CoachLogsService } from '../../services/coach-logs.service';
import { ClientService } from '../../services/ClientService';
import * as XLSX from 'xlsx';
import { HttpResponse } from '@angular/common/http';
import { th } from 'date-fns/locale';
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
    clientId: '',
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
  headerRow: any = [];
  coachingLogsFromExcel: any = [];
  showErrorMessage: boolean = false;

  deleteThisCoachingLogs: any = [];
  clients: any;
  coachId: any;

  constructor(
    private coachLogsService: CoachLogsService,
    private clientService: ClientService,
    ) { }

  ngOnInit(): void {

    this.coachId = JSON.parse(sessionStorage.getItem('user')!).id;
    this.userRole = JSON.parse(sessionStorage.getItem('user')!).userRole;
    this.getCoachingLogs(0);
    this.getClients(0);
  }

  getCoachingLogs(page: any) {
    this.loading = true;
    this.page = page;
    //if page is 0, don't subtract 1
    if (page === 0 || page < 0) {
      page = 0;
    } else {
      page = page - 1;
    }
    
    const options = {
      page: page,
      sort: 'id,desc',
      size: this.itemsPerPage,
      search: this.filters.searchItem,
    };
    this.coachLogsService.getCoachLogs(options).subscribe((response: any) => {
      this.coachingLogs = response.body;
      this.totalElements = response.headers.get('X-Total-Count');
      this.loading = false;
    }, (error: any) => {
      this.loading = false;
    }
    );

  }


  onTableDataChange(event: any) {
    this.page = event;
    this.getCoachingLogs(this.page);
  }

  addCoachingLog() {
    this.loading = true;
    let log = [this.coachingLog]
    this.coachLogsService.addCoachLogs(log).subscribe({
      next: (response) => {
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
        this.excelData = XLSX.utils.sheet_to_json(workbook.Sheets[sheetNames[0]]);
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


    this.coachLogsService.addCoachLogs(this.coachingLogsFromExcel).subscribe({
      next: (response) => {
        this.loading = false;
        this.showErrorMessage = false;
        this.clearAndClose();
        this.getCoachingLogs(0);
      }, error: (error) => {
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

  //select all checkboxes and push coachingLogs ids of the list to deleteThisCoachingLogs
  selectAll(event: any) {
    let checkboxes = document.querySelectorAll('input[type="checkbox"]');
    if (event.target.checked) {
      checkboxes.forEach((checkbox: any) => {
        checkbox.checked = true;
      });
      for (let i = 0; i < this.coachingLogs.length; i++) {
        this.deleteThisCoachingLogs.push(this.coachingLogs[i].id);
      }
    } else {
      checkboxes.forEach((checkbox: any) => {
        checkbox.checked = false;
        this.deleteThisCoachingLogs = [];
      });
    }
  }
  
  selectOne(event: any, id: any) {
    let checkboxes = document.querySelectorAll('input[type="checkbox"]');
    if (event.target.checked) {
      checkboxes.forEach((checkbox: any) => {
        if (checkbox.value === id) {
          checkbox.checked = true;
        }
      });
      this.deleteThisCoachingLogs.push(id);
    } else {
      checkboxes.forEach((checkbox: any) => {
        if (checkbox.value === id) {
          checkbox.checked = false;
        }
      });
    }
  }

  deleteOne(id: any) {
    this.deleteThisCoachingLogs = [];
    this.deleteThisCoachingLogs.push(id);
    this.deleteCoachingLog();
  }

  deleteCoachingLog() {
    this.loading = true;
    const options = {
      coachingLogIds: this.deleteThisCoachingLogs,
    };
    this.coachLogsService.deleteCoachLogs(options).subscribe({
      next: (response: any) => {
        this.loading = false;
        this.deleteThisCoachingLogs = [];
        this.getCoachingLogs(0);
      }
    });
  }

  getClients(page: any) {
    this.loading = true;
    this.page = page;
    //if page is 0, don't subtract 1
    if (page === 0 || page < 0) {
      page = 0;
    } else {
      page = page - 1;
    }
    if(this.filters.status == 'ALL'){
      this.filters.status = '';
    }
    const options: any = {
      page: page,
      size: this.pageSize,
      status: this.filters.status,
      search: this.filters.searchItem,
      sort: 'id,desc',
    };

    if (this.userRole == 'COACH') {
      options.coachId = this.coachId;
    }
    
    this.clientService.getClients(options).subscribe(  // test the getAllOrgClients endpoint
      (response) => {
        this.loading = false;
        this.clients = response.body;
        for (let client of this.clients) {
          if (client.userRole != 'CLIENT') {
            this.clients.splice(this.clients.indexOf(client), 1);
          }
        }
        this.totalElements = +response.headers.get('X-Total-Count');
      }, (error) => {
        this.loading = false;
      }
    )
  }

}
