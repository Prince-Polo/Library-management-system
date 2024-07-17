import { SeatMessage } from 'Plugins/SeatAPI/SeatMessage'

export class SeatQueryMessage extends SeatMessage {
    floor: string;
    section: string;
    seatNumber: string;
    constructor( floor: string,
                 section:string,
                 seatNumber: string) {
        super();
        this.floor=floor;
        this.section=section;
        this.seatNumber=seatNumber;
    }
}
