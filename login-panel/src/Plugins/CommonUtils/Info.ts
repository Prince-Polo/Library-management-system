export class Info {
    name: string;
    password:string;
    email: string;
    number: string;

    constructor(name: string, email: string, number: string, password: string) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.password = password;
    }
}
export class LoginInfo {
    name: string;
    password:string;
    constructor(name: string, password: string) {
        this.name = name;
        this.password = password;
    }
}