import React from 'react';
import {useHistory} from 'react-router';
import {RegisterLoginForm} from './RegisterLoginForm';
import { StudentLoginMessage } from 'Plugins/StudentAPI/StudentLoginMessage';
import { StudentRegisterMessage } from 'Plugins/StudentAPI/StudentRegisterMessage';
//import { AdminRegisterMessage } from 'Plugins/AdminAPI/AdminRegisterMessage';
import { AdminLoginMessage } from 'Plugins/AdminAPI/AdminLoginMessage';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser, faLock, faIdCard, faUserShield } from '@fortawesome/free-solid-svg-icons';

export function StudentRegister() {
    const fields = [
        { name: 'name', type: 'text', label: 'Name', icon: faUser },
        { name: 'password', type: 'password', label: 'Password', icon: faLock },
        { name: 'number', type: 'textarea', label: 'Number', icon: faIdCard }
    ];
    const createMessage = (formData: any) => {
        return new StudentRegisterMessage(formData.name, formData.password, formData.number);
    };

    return (
        <div style={{ background: 'aliceblue', opacity: '90%' }}>
            <RegisterLoginForm title="Student Register" fields={fields} createMessage={createMessage} />
        </div>
    );
}

export function StudentLogin() {
    const fields = [
        { name: 'name', type: 'text', label: 'Name', icon: faUser },
        { name: 'password', type: 'password', label: 'Password', icon: faLock },
        { name: 'number', type: 'textarea', label: 'Number', icon: faIdCard }
    ];
    const createMessage = (formData: any) => {
        return new StudentLoginMessage(formData.name, formData.password, formData.number);
    };

    return (
        <div style={{ background: 'white', opacity: '90%' }}>
                <RegisterLoginForm title="Student Login" fields={fields} createMessage={createMessage} isAdmin={false}/>
        </div>
    );
}

// export function AdminRegister() {
//     const fields = [
//         { name: 'name', type: 'text', label: 'Name', icon: faUserShield },
//         { name: 'password', type: 'password', label: 'Password', icon: faLock },
//         { name: 'id', type: 'textarea', label: 'ID', icon: faIdCard }
//     ];
//     const createMessage = (formData: any) => new AdminRegisterMessage(formData.name, formData.password, formData.id);
//
//     return (
//         <div style={{ background: 'lightcyan', opacity: '90%', color: '#8B4513' }}>
//             <RegisterLoginForm title="Administrator Register" fields={fields} createMessage={createMessage} isAdmin={true}/>
//         </div>
//     );
// }

export function AdminLogin() {
    const history = useHistory();
    const fields = [
        { name: 'name', type: 'text', label: 'Name', icon: faUserShield },
        { name: 'password', type: 'password', label: 'Password', icon: faLock }
    ];
    const handleSuccess = () => {
        setTimeout(() => history.push('/addJob'), 2000); // 跳转到 AddJobPage
    };
    const createMessage = (formData: any) => new AdminLoginMessage(formData.name, formData.password);

    return (
        <div style={{ background: 'lightyellow', opacity: '90%', color: '#8B4513' }}>
            <RegisterLoginForm title="Administrator Login" fields={fields} createMessage={createMessage} onSuccess={handleSuccess} isAdmin={true}/>
        </div>
    );
}
