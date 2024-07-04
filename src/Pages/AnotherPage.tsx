import React from 'react'
import { useHistory } from 'react-router'
import myImage from './cracking.png'

const products = [
    { title: 'Banteng milk', isFruit: false, isVeg: false,id: 1 },
    { title: 'Garlic', isFruit: false, isVeg: true,id: 2 },
    { title: 'Apple', isFruit: true, isVeg: false, id: 3 },
];
function MyComponent(s: string) {
    const titleStyle = {
            color: s,
            fontSize: '24px',
            fontWeight: 'bold',
    }
    return (
        <h1 style={titleStyle}>ga</h1>
    );
}
export default function ShoppingList() {
    const listItems = products.map(product =>
        <li
            key={product.id}
            style={{
                color: product.isFruit ? 'magenta' : product.isVeg ? 'darkgreen':'blueviolet'
            }}
        >
            {product.title}
        </li>
    );
    return (
        <ul>{listItems}</ul>
    );
}
export function AnotherPage(){
    const history=useHistory()
    const handleClick=()=>{window.location.href="https://chatglm.cn"}
    const handleChick=()=>{alert("There flies a Guinea fowl.")}

    return (<div>
        <h1 style={{marginTop:'30px',marginLeft:'40px'}}>
            Hahaha!
        </h1>
        <hr></hr>
        <h2>
            This is naive front end, Kuru!<br />
            If you see this page, you know you are a banteng!<br />
        </h2>
        This primitive page is still under development, though, banteng!<br />
        For more information, see <br />
        <button onClick={handleClick}>
          this.
        </button>
        <button onClick={handleChick}>
            something else
        </button>
        <h2>Local Image Example</h2>
        <img src={myImage}
            alt={'Cracking reactor in Baki.'}
             style={{marginLeft:'10px',width:626,height:700}}></img>
        <h1>
            False!
        </h1>
        <p>How come you made such a monstrous mistake!<br />
            Plough your fields on Java, banteng! Your species is under threat there in Java, so why not go back and
            contribute to your race, banteng!
        </p>
        <button onClick={() => history.push("..")}>
            back to the last page
        </button>
        <button onClick={() => history.push("/library")}>
            to the next page
        </button>
        <ShoppingList></ShoppingList>
        You banteng! Quit now!<br />
    </div>)
}


