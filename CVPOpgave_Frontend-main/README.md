# Velkommen til CVP frontend

Dette projekt er baseret på Angular, som gør brug af TypeScript.
Der er tidligere blevet brugt GitBash til at kører de forskellige kommandoer fra og Visual Studio Code som IDE til dette projekt.
Det kræves at man har NodeJS (nyeste stable version) installeret for at kunne køre projektet.

Når repositoriet skal der navigeres til CVP_Frontend folderen (her er der blevet brugt GitBash). 
Herfra skal der installeres de fornødne node-modules ved at køre følgende kommando:

  'npm install'

Der skal i øvrigt også tilføjes en TypeScript-fil i 'scr' mappen ('/CVP_Frontend/scr/') med navnet 'URL_Credentials.ts' med følgende indhold:
__________________________________

    export class Url_credentials{ 
    
        // URL for webservice
        private webserviceURLString = 'http://XXXX:XXXX/'; 
     
    
        constructor(){ 
    
        } 
     
    
        public get webserviceURL(){ 
    
            return this.webserviceURLString; 
    
        } 
    
    }
___________________________________

'XXXX:XXXX' skal byttes ud med den URL og port som CVP's webservice kører på.

I GitBash stående i 'CVP_Frontend'-folderen køres projektet med følgende kommando:

  'ng serve'

Default porten er 4200, man kan ændres hvis ønskes. 
