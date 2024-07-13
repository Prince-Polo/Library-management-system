import React from 'react';
import { Link } from 'react-router-dom';
import './LibrarySelector.css';

const libraries = [
    { name: 'Main Library North', total: 900, available: 725 },
    { name: 'Main Library West', total: 588, available: 496 },
    { name: 'Humanities Library', total: 462, available: 356 },
    { name: 'Law Library', total: 639, available: 522 },
    { name: 'Arts Library', total: 184, available: 183 },
    { name: 'Finance Library', total: 97, available: 94 },
];

const LibrarySelector = () => (
    <div className="library-selector">
        <h1>Library Seat Management System</h1>
        <div className="libraries">
            {libraries.map((library, index) => (
                <div className="library" key={index}>
                    <h2>{library.name}</h2>
                    <p>Available: {library.available}, Total: {library.total}</p>
                    <Link to={`/seats/${index}`} className="seat-button">Select Seats</Link>
                </div>
            ))}
        </div>
        <Link to="/mycenter" className="my-center-button">My Center</Link>
    </div>
);

export default LibrarySelector;