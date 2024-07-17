import { StudentMessage } from 'Plugins/StudentAPI/StudentMessage'

export class StudentReservationMessage extends StudentMessage {
    studentNumber:string;
    floor: string;
    section: string;
    seatNumber: string;
    constructor( studentNumber:string,
                 floor: string,
                 section:string,
                 seatNumber: string
    ) {
        super();
        this.floor=floor;
        this.section=section;
        this.seatNumber=seatNumber;
        this.studentNumber=studentNumber;
    }
}