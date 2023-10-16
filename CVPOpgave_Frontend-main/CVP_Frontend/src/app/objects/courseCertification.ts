export class CourseCertification{
    title:String;
    startDate:Date;
    endDate:Date;
    
    constructor(
        title:String,
        startDate:Date,
        endDate:Date
        ){
            this.title = title;
            this.startDate = startDate;
            this.endDate = endDate;
    }
}