import React from 'react'
import { render } from 'react-dom'
import { HashRouter, Route, Switch } from 'react-router-dom'
import { Main } from 'Pages/Main'
import { AnotherPage } from 'Pages/AnotherPage'
import { Library } from 'Pages/Library'
import {StudentRegister,StudentLogin,AdminLogin,AdminRegister} from 'Pages/Register'
import  AdminPage  from 'Pages/AdminPage'
const Layout = () => {
    return (
        <HashRouter>
            <Switch>
                <Route path="/" exact component={Main} />
                <Route path="/another" exact component={AnotherPage} />
                <Route path={"/library"} exact component={Library}/>
                <Route path={"/StudentRegister"} exact component={StudentRegister}/>
                <Route path={"/Admin"}exact component={AdminPage}/>
                <Route path={"/StudentLogin"} exact component={StudentLogin}/>
                <Route path={"/AdminRegister"} exact component={AdminRegister}/>
                <Route path={"/AdminLogin"} exact component={AdminLogin}/>
            </Switch>
        </HashRouter>
    )
}
render(<Layout />, document.getElementById('root'))