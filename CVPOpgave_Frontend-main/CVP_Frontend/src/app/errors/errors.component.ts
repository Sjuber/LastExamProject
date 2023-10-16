import { Component, Inject, Input, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-errors',
  templateUrl: './errors.component.html',
  styleUrls: ['./errors.component.css']
})
export class ErrorsComponent implements OnInit {
  closeResult: string = '';
   @Input() error:string = "";

   
    
  /*------------------------------------------
  --------------------------------------------
  Created constructor
  --------------------------------------------
  --------------------------------------------*/
  constructor(private modalService: NgbModal , private dialogRef: MatDialogRef<ErrorsComponent>, @Inject(MAT_DIALOG_DATA) data:any){
      this.error = data.error;
      console.log(this.error);
  }
    
  /**
   * Write code on Method
   *
   * @return response()
   */
  // open(content:any) {
  //   // if(this.error!="" || this.error != undefined){
  //    // let content = document.getElementsByTagName("mymodal");
  //   this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
  //    this.closeResult = `Closed with: ${result}`;
  //   }, (reason) => {
  //     this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
  //   });
  // } 
// }
    
  /**
   * Write code on Method
   *
   * @return response()
   */
  // private getDismissReason(reason: any): string {
  //   if (reason === ModalDismissReasons.ESC) {
  //     return 'by pressing ESC';
  //   } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
  //     return 'by clicking on a backdrop';
  //   } else {
  //     return  `with: ${reason}`;
  //   }
  // }

  close() {
    this.dialogRef.close();
}
  
  ngOnInit(): void {
  }
}
