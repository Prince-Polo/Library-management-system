// src/index.tsx 或 src/Layout.tsx

import React from 'react';
import { render } from 'react-dom';
import { HashRouter, Route, Switch } from 'react-router-dom';
import { Main } from 'Pages/MainPages/Main';
import { AnotherPage } from 'Pages/MainPages/AnotherPage';
import { StudentRegister, StudentLogin, AdminLogin } from 'Pages/MainPages/RegisterLogin'
import AddJobPage from 'Pages/AdminPages/AddJobPage';
import BrowseJobsPage from 'Pages/AdminPages/BrowseJobsPage';
import CheckSeatsPage from 'Pages/AdminPages/CheckSeats';
import ViewSeatsPage from 'Pages/AdminPages/ViewSeatsPage';
import { StudentPage } from 'Pages/StudentPages/StudentPage';
import { MyCenter } from 'Pages/StudentPages/MyCenter'
import { AreaSelection } from 'Pages/StudentPages/AreaSelection';
import { AreaPage } from 'Pages/StudentPages/AreaPage';
import { ReportIssue } from 'Pages/StudentPages/ReportPage'
import { VolunteerApplication } from 'Pages/StudentPages/VolunteerApplication';

const Layout = () => {
    return (
        <HashRouter>
            <Switch>
                <Route path="/" exact component={Main} />
                <Route path="/another" exact component={AnotherPage} />
                <Route path="/StudentRegister" exact component={StudentRegister} />
                <Route path="/StudentLogin" exact component={StudentLogin} />
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
