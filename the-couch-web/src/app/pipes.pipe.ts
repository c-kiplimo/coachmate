import { Pipe, PipeTransform } from '@angular/core';
import { DatePipe } from '@angular/common';

@Pipe({
  name: 'pipes'
})
export class PipesPipe implements PipeTransform {

  transform(value: any): unknown {
    let period:any ;
    // "11/10/2016, 11:49:36 AM"
    let today:String = new Date().toLocaleString().slice(0,10);
    let query:String = new Date(value).toLocaleString().slice(0,10);
    let dayDiff:number = Number(query.split("/")[1]) - Number(today.split("/")[1]);
    let monthDiff:number = Number(query.split("/")[0]) - Number(today.split("/")[0]);
    if (value){
      // console.log("Entered Date: ",new Date(value));
      // console.log("Current Date: ",new Date());
      period =  Math.floor((Number(new Date()) - Number(new Date(value)))/1000);
      // console.log("Difference in Sec: ", period);

      // Tomorrow  | within 24 hours, less than 0
      if( today != query && period < 0 && dayDiff === 1 && monthDiff === 0){
        // console.log("Tomorrow");
        return "Tomorrow";
      }
      // After Tommorow > Greater than 24hrs
      if(today != query && period < -86401){
        // console.log("After Tomorrow");
        return query;
      }
      // Yesterday
      if(today != query && period > 0 && dayDiff === -1 && monthDiff === 0){
        console.log("Yesterday");
        return "Yesterday";
      }
      // further down the line
       if(period > 86400){
        console.log("Before Yesterday");
        return query;
      }
    }
    console.log("Today I suppose");
    return "Today";
  }

}

