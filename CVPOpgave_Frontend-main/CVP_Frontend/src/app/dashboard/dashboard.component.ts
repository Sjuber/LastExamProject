import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { CvsService } from '../cvs.service';
import { CV } from '../objects/cv';
import { SimpleCV } from '../objects/simpleCV';
import { ShowSingleCVComponent } from '../show-single-cv/show-single-cv.component';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  allOriginalSimpleCVs: SimpleCV[] = [];
  originalSimpleCV: SimpleCV | undefined;
  chosedCV: CV | undefined;
  noCVsMessage: string = "";

  constructor(private cvsService: CvsService, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.getAllCVsNotInDraftmode();
  }

  getAllCVsNotInDraftmode() {
    this.cvsService.getAllCVsNotInDraftmode().subscribe({
      next: (cvs) => {
        this.allOriginalSimpleCVs = cvs.reverse();
        this.noCVsMessage = "";

        if (this.allOriginalSimpleCVs.length == 0) {
          this.noCVsMessage = "(Der kunne ikke hentes CV'er - prøv senere eller refresh siden)";
        } else {
          this.noCVsMessage = "";
        }
      },
      error: (e) => {
        console.error(e);
        this.noCVsMessage = "(Der kunne ikke hentes CV'er - prøv senere eller refresh siden)";
      }
    }
    );
  }

  showOriginalCV(cvChosed: SimpleCV) {
     // TODO compare on title+user
    if (this.originalSimpleCV != undefined
      && (this.originalSimpleCV.bookedHours != cvChosed.bookedHours
        || this.originalSimpleCV.maxHours != cvChosed.maxHours
        || this.originalSimpleCV.consultantName != cvChosed.consultantName
        )) {
      this.originalSimpleCV = cvChosed;

  // TODO compare on title+user
      if (this.originalSimpleCV?.consultantPhone) {
        this.cvsService.getCVByUserPhoneAndSimpleCV(this.originalSimpleCV,this.originalSimpleCV?.consultantPhone).subscribe({
          next: (cvs) => {
            this.chosedCV = cvs;

            if (cvChosed == undefined) {
              this.noCVsMessage = "CV kunne ikke hentes";
            } else {
              this.noCVsMessage = "";
            }
          },
          error: (e) => {
            console.error(e);
          }
        });
      }
    }
    else if (this.originalSimpleCV == undefined) {
      this.originalSimpleCV = cvChosed;

    }


    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      error: this.noCVsMessage,
      cv: this.originalSimpleCV
    };
    dialogConfig.width = '70%';
    dialogConfig.height = '90%';
    let dialogRef = this.dialog.open(ShowSingleCVComponent, dialogConfig);

  }
}

