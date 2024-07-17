import { SeatMessage } from 'Plugins/SeatAPI/SeatMessage'

export class QuerySectionsByFloorMessage extends SeatMessage{
    floor: String;
    constructor(floor: String) {
        super();
        this.floor = floor;
    }
}