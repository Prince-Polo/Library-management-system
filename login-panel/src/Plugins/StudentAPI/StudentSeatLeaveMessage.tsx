import { StudentMessage } from 'Plugins/StudentAPI/StudentMessage'

export class StudentSeatLeaveMessage extends StudentMessage {
    token: string;
    floor: string;
    section: string;
    seatNumber: string
    constructor(token: string,floor: string, section: string, seatNumber: string) {
        super();
        this.floor = floor;
        this.section = section;
        this.seatNumber = seatNumber;
        this.token = token;
    }
}
