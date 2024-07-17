import { StudentMessage } from 'Plugins/StudentAPI/StudentMessage'
import {SeatInfo} from 'Plugins/CommonUtils/Info'

export class StudentLeaveMessage extends StudentMessage {
    studentNumber: string
    floor: string
    section: string
    seatNumber: string
    constructor(studentNumber:string,floor:string,section:string,seatNumber:string){
        super()
        this.studentNumber=studentNumber
        this.floor=floor
        this.section=section
        this.seatNumber=seatNumber
    }
}