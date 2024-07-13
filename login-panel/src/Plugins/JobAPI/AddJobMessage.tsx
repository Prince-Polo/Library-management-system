import { JobMessage } from 'Plugins/JobAPI/JobMessage'

export class AddJobMessage extends JobMessage{
  //  JobId: string;
  //  JobStudentId:string;
    JobShortDescription: string;
    JobLongDescription:string;
    JobHardness:string;
    JobCredit:string;
    JobRequired:string;
   /* JobComplete:boolean;
    JobBooked:boolean;
    JobApproved:boolean;*/




    constructor(JobShortDescription:string,JobLongDescription:string,JobHardness:string,JobCredit:string,JobRequired:string) {
        super();
      //  this.JobId = JobId;
     //   this.JobStudentId = JobStudentId;
        this.JobShortDescription=JobShortDescription;
        this.JobLongDescription=JobLongDescription;
        this.JobHardness=JobHardness;
        this.JobCredit=JobCredit;
        this.JobRequired=JobRequired;
       /* this.JobComplete=JobComplete;
        this.JobBooked=JobBooked;
        this.JobApproved=JobApproved;*/


    }
}
