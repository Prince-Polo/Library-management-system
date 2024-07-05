import React from 'react';
import { useHistory } from 'react-router'
import './app.css'
export function Admin(){
    const history=useHistory()
    return(<div style={{color:'blue',fontFamily:'宋体',fontSize:'20px'}}>退出吧！
    <button onClick={()=>history.push("/")} className={'button-primary'}>back</button></div>)
}