export class PractisedLanguage{
    name:String;
    levelWritting?:String;
    levelReading?:String;
    
    constructor(
        name:String,
        levelWritting?:String,
        levelReading?:String
        ){
            this.name = name;
            this.levelWritting = levelWritting;
            this.levelReading = levelReading;
    }
}