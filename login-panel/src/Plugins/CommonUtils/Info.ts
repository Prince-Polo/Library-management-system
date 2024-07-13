export class BasicInfo {
    userName: string;
    password:string;
    number: string;

    constructor(name: string, password: string,number: string) {
        this.userName = name;
        this.number = number;
        this.password = password;
    }
}
export class Info{
    valid: boolean|undefined;
    userName: string|null;
    number: string|null;
    volunteerStatus: string|null;
    floor: string|null;
    sectionNumber: string|null;
    seatNumber: string|null;
    violationCount:string|null;
    volunteerHours: string|null;
}
export class LoginInfo {
    name: string;
    password:string;
    constructor(name: string, password: string) {
        this.name = name;
        this.password = password;
    }
}
export class SeatInfo{
    floor: number
    section: number
    seatNumber: number
    constructor(floor:number,section:number,seatNumber:number){
        this.floor=floor
        this.section=section
        this.seatNumber=seatNumber
    }
}