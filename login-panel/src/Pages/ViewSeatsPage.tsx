import React, { useState } from 'react';
import AdminLayout from './AdminLayout';

interface Seat {
    floor: string;
    section: string;
    seatNumber: string;
    status: string;
    occupied: string;
    studentNumber: string;
}

interface QuerySeatsInSectionResponse {
    totalSeats: string;
    freeSeats: string;
    seats: Seat[];
}

const ViewSeatsPage: React.FC = () => {
    const [error, setError] = useState<string | null>(null);
    const [seats, setSeats] = useState<Seat[]>([]);
    const [floor, setFloor] = useState<string>('');
    const [section, setSection] = useState<string>('');
    const [selectedSeats, setSelectedSeats] = useState<Set<string>>(new Set());

    const fetchSeats = async (floor: string, section: string) => {
        setError(null);

        const message = {
            floor,
            section
        };

        try {
            const response = await fetch('http://127.0.0.1:10004/api/Seat/QuerySeatsInSectionMessage', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(message)
            });

            let result = await response.text();
            let parsedResult;
            try {
                parsedResult = JSON.parse(result);
                parsedResult = JSON.parse(parsedResult);
            } catch (e) {
                console.error('Failed to parse response JSON:', e);
                throw new Error('Failed to parse response JSON');
            }

            setSeats(parsedResult.seats);

        } catch (error) {
            setError('Failed to fetch seats');
            console.error('Error fetching seats:', error);
        }
    };

    const toggleOccupySeat = async (seat: Seat) => {
        const message = {
            floor: seat.floor,
            section: seat.section,
            seatNumber: seat.seatNumber
        };

        try {
            if (seat.occupied === 'false') {
                await fetch('http://127.0.0.1:10004/api/Seat/UpdateSeatOccupiedStatus', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(message)
                });
            } else {
                await fetch('http://127.0.0.1:10004/api/Seat/UpdateSeatReleasedStatus', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(message)
                });
            }

            fetchSeats(floor, section);

        } catch (error) {
            setError('Failed to update seat status');
            console.error('Error updating seat status:', error);
        }
    };

    const handleMultiToggle = async () => {
        for (const seatNumber of selectedSeats) {
            const seat = seats.find(seat => seat.seatNumber === seatNumber);
            if (seat) {
                await toggleOccupySeat(seat);
            }
        }
        setSelectedSeats(new Set());
    };

    const handleSearch = () => {
        if (floor && section) {
            fetchSeats(floor, section);
        }
    };

    const handleSelectSeat = (seatNumber: string) => {
        setSelectedSeats(prevSelectedSeats => {
            const newSelectedSeats = new Set(prevSelectedSeats);
            if (newSelectedSeats.has(seatNumber)) {
                newSelectedSeats.delete(seatNumber);
            } else {
                newSelectedSeats.add(seatNumber);
            }
            return newSelectedSeats;
        });
    };

    return (
        <AdminLayout>
            <h2 style={headingStyle}>View Seats in Section</h2>
            {error && <p style={errorMessageStyle}>{error}</p>}
            <div style={searchContainerStyle}>
                <input
                    type="text"
                    value={floor}
                    onChange={(e) => setFloor(e.target.value)}
                    placeholder="Floor"
                    style={inputStyle}
                />
                <input
                    type="text"
                    value={section}
                    onChange={(e) => setSection(e.target.value)}
                    placeholder="Section"
                    style={inputStyle}
                />
                <button onClick={handleSearch} style={searchButtonStyle}>Search</button>
            </div>
            <table style={seatsTableStyle}>
                <thead>
                <tr>
                    <th style={thStyle}></th>
                    <th style={thStyle}>Seat Number</th>
                    <th style={thStyle}>Status</th>
                    <th style={thStyle}>Occupied</th>
                    <th style={thStyle}>Student Number</th>
                    <th style={thStyle}>Actions</th>
                </tr>
                </thead>
                <tbody>
                {seats
                    .sort((a, b) => parseInt(a.seatNumber) - parseInt(b.seatNumber))
                    .map((seat) => (
                        <tr key={seat.seatNumber}>
                            <td style={tdStyle}>
                                <input
                                    type="checkbox"
                                    checked={selectedSeats.has(seat.seatNumber)}
                                    onChange={() => handleSelectSeat(seat.seatNumber)}
                                />
                            </td>
                            <td style={tdStyle}>{seat.seatNumber}</td>
                            <td style={tdStyle}>{seat.status}</td>
                            <td style={tdStyle}>{seat.occupied}</td>
                            <td style={tdStyle}>{seat.studentNumber}</td>
                            <td style={tdStyle}>
                                <button onClick={() => toggleOccupySeat(seat)} style={buttonStyle}>
                                    {seat.occupied === 'false' ? 'Occupy' : 'Release'}
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <button onClick={handleMultiToggle} style={multiToggleButtonStyle}>Toggle Selected</button>
        </AdminLayout>
    );
};

const headingStyle: React.CSSProperties = {
    marginBottom: '20px',
    fontFamily: 'Segoe UI, Tahoma, Geneva, Verdana, sans-serif',
    color: '#333',
};

const searchContainerStyle: React.CSSProperties = {
    marginBottom: '20px',
    display: 'flex',
    gap: '10px',
};

const inputStyle: React.CSSProperties = {
    padding: '10px',
    fontSize: '16px',
    borderRadius: '5px',
    border: '1px solid #ddd',
    flex: '1',
};

const searchButtonStyle: React.CSSProperties = {
    padding: '10px 20px',
    fontSize: '16px',
    borderRadius: '5px',
    border: 'none',
    background: '#007bff',
    color: 'white',
    cursor: 'pointer',
};

const seatsTableStyle: React.CSSProperties = {
    width: '100%',
    borderCollapse: 'collapse',
    marginTop: '20px',
};

const thStyle: React.CSSProperties = {
    border: '1px solid #ddd',
    padding: '8px',
    background: '#f0f8ff',
    fontWeight: 'bold',
};

const tdStyle: React.CSSProperties = {
    border: '1px solid #ddd',
    padding: '8px',
};

const buttonStyle: React.CSSProperties = {
    background: '#007bff',
    color: 'white',
    border: 'none',
    padding: '5px 10px',
    borderRadius: '5px',
    cursor: 'pointer',
};

const multiToggleButtonStyle: React.CSSProperties = {
    background: '#32cd32',
    color: 'white',
    border: 'none',
    padding: '10px 15px',
    borderRadius: '5px',
    cursor: 'pointer',
    marginTop: '20px',
};

const errorMessageStyle: React.CSSProperties = {
    color: 'red',
    fontWeight: 'bold',
};

export default ViewSeatsPage;
