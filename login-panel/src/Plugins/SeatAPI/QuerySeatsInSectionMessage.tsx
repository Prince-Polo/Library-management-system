import { SeatMessage } from 'Plugins/SeatAPI/SeatMessage'

export class QuerySeatsInSectionMessage extends SeatMessage{
    floor: String;
    section: String;
    constructor(floor: String,section: string) {
        super();
        this.floor = floor;
        this.section = section;
    }
}