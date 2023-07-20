import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { AttachmentService } from '../../services/AttachmentService';
@Component({
  selector: 'app-attachments',
  templateUrl: './attachments.component.html',
  styleUrls: ['./attachments.component.css']
})
export class AttachmentsComponent implements OnInit {
  attachmentForm!: FormGroup;
  constructor(
    private formBuilder: FormBuilder,private attachmentService: AttachmentService ,private http: HttpClient
  ) { }

  ngOnInit(): void {
    this.attachmentForm = this.formBuilder.group({
      sessionId: ['', Validators.required],
      attachmentType: ['', Validators.required],
      attachment: ['', Validators.required]
    })
  }
//   submitForm() {
//     const formData = new FormData();
//     formData.append('sessionId', this.attachmentForm.value.sessionId);
//     formData.append('attachmentType', this.attachmentForm.value.attachmentType);
//     formData.append('attachment', this.attachmentForm.value.attachment);

//     this.attachmentService.addAttachment(formData).subscribe(
//       (response) => {
//         console.log(response);
//         // Handle success
//       },
//       (error) => {
//         console.log(error);
//         // Handle error
//       }
//     );
//   }

}

