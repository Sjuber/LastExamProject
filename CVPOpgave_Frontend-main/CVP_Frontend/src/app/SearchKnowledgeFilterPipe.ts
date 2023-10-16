import { Pipe, PipeTransform } from '@angular/core';
import { Knowledge } from './objects/knowledge';

@Pipe({
  name: 'searchFilter'
})
export class SearchFilterPipe implements PipeTransform {
  transform(options:Knowledge[] , searchTerm:string) {
    options.sort(
      (aCVKnowledge:Knowledge,bCVKnowledge: Knowledge ) =>  
      aCVKnowledge.knowCategoryDTO.name > bCVKnowledge.knowCategoryDTO.name ? 1 : - 1);
    if (!options || !searchTerm) {
      return options;
    }
    return options.filter(option => {
      let categoryAndKnowledge:string = "";
      if(option.name != undefined){
        categoryAndKnowledge = option.knowCategoryDTO.name.concat(option.name);
      }
      return categoryAndKnowledge.toLowerCase().trim().indexOf(searchTerm.toLowerCase().trim()) !== -1;
    });
  }
}