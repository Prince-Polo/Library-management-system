import { SeatMessage } from 'Plugins/SeatAPI/SeatMessage'

export class SeatReportMessage extends SeatMessage {
    floor: string;
    section: string;
    seatNumber: string;
    feedback: string;
    constructor( floor: string,
                 section:string,
                 seatNumber: string,
                 feedback: string,) {
        super();
        this.floor=floor;
        this.section=section;
        this.seatNumber=seatNumber;
        this.feedback=feedback;
    }
}
