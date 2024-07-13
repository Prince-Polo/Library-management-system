import { StudentMessage } from 'Plugins/StudentAPI/StudentMessage'
import {SeatInfo} from 'Plugins/CommonUtils/Info'

export class StudentLoginMessage extends StudentMessage {
    floor: number
    section: number
    seatNumber: number
    constructor(floor:number,section:number,seatNumber:number){
        super()
        this.floor=floor
        this.section=section
        this.seatNumber=seatNumber
    }
}