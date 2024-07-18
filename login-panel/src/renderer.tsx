// src/index.tsx 或 src/Layout.tsx

import React from 'react';
import { render } from 'react-dom';
import { HashRouter, Route, Switch } from 'react-router-dom';
import { Main } from 'Pages/Main';
import AnotherPage from 'Pages/AnotherPage';
import { Library } from 'Pages/Library';
import { StudentRegister, StudentLogin, AdminLogin, AdminRegister } from 'Pages/Register';
import AddJobPage from 'Pages/AddJobPage';
import BrowseJobsPage from 'Pages/BrowseJobsPage';
import StudentPage from 'Pages/StudentPage';
import {MyCenter} from 'Pages/MyCenter'
import {AreaSelection} from 'Pages/AreaSelection'
import {ReportIssue} from 'Pages/ReportPage'
import VolunteerApplication from 'Pages/VolunteerApplication';
import CheckSeatsPage from 'Pages/CheckSeats';
import ViewSeatsPage from 'Pages/ViewSeatsPage';
import {AreaPage} from 'Pages/AreaPage';
import "./Pages/Styles/app.css"
import "./Pages/Styles/SeatSelection.css"

const Layout = () => {
    return (
        <HashRouter>
            <Switch>
                <Route path="/" exact component={Main} />
                <Route path="/another" exact component={AnotherPage} />
                <Route path="/library" exact component={Library} />
                <Route path="/StudentRegister" exact component={StudentRegister} />
                <Route path="/StudentLogin" exact component={StudentLogin} />
                <Route path="/AdminRegister" exact component={AdminRegister} />
                <Route path="/AdminLogin" exact component={AdminLogin} />
                <Route path="/AddJob" exact component={AddJobPage} />
                <Route path="/BrowseJobs" exact component={BrowseJobsPage} />
                <Route path="/Student" exact component={StudentPage} />
                <Route path={"/mycenter"} component={MyCenter}/>
                <Route path={"/areas/:library"} component={AreaSelection}/>
                <Route path={"/Report"} exact component={ReportIssue}/>
                <Route path="/CheckSeats" component={CheckSeatsPage} />
                <Route path="/VolunteerApplication" exact component={VolunteerApplication} />
                <Route path={"/area/:floor/:area"} component={AreaPage}/>
                <Route path="/ViewSeats" component={ViewSeatsPage} />
            </Switch>
        </HashRouter>
    );
};

render(<Layout />, document.getElementById('root'));
