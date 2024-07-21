import React, { useState, useEffect, useCallback } from 'react';
import { useHistory, useLocation } from 'react-router';
import { SeatReservationModal } from './SeatSelection';
import { ErrorModal } from 'Pages/SharedComponents/ErrorMessage';
import { useSeatData } from 'Pages/SharedComponents/LibraryDataLinking';
import images from 'Pages/SharedComponents/ImageLoader';

export type AreaSetting = {
    name: string;
    index: number;
    area: string;
};

export const AreaPage: React.FC = () => {
    const location = useLocation<AreaSetting>();
    const history = useHistory();
    const { name, index, area } = location.state || { name: 'Unknown Library', index: -1, area: 'A' };
    const [selectedSeat, setSelectedSeat] = useState<string | null>(null);
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [isSeatOccupied, setIsSeatOccupied] = useState<boolean>(false);
    const [isSeatReported, setIsSeatReported] = useState<string>('');
    const [isSeatConfirmed, setIsSeatConfirmed] = useState<boolean>(false);
    console.log("Area page entered.");
    const { seats, fetchSeats, error: seatError, clearError: clearSeatError } = useSeatData();
    console.log('Location state:', location.state);

    useEffect(() => {
        console.log('useEffect triggered with index and area:', { index, area }); // 调试信息
        if (index !== -1 && area) {
            console.log('Fetching seats for:', { index, area }); // 调试信息
            fetchSeats(index.toString(), area);
        }
    }, [index, area, fetchSeats]);

    const handleSeatClick = useCallback((seatNumber: string, occupied: boolean, reported: string, confirmed:boolean) => {
        setSelectedSeat(seatNumber);
        setIsSeatOccupied(occupied);
        setIsSeatReported(reported);
        setIsSeatConfirmed(confirmed);
        setIsModalOpen(true);
    }, []);

    const handleCloseModal = useCallback(() => {
        setIsModalOpen(false);
        fetchSeats(index.toString(), area);
    }, [index,area,fetchSeats]);

    const getImage = useCallback(() => {
        return images[`F${index}${area}.png`] || images[`cracking.png`];
    }, [index, area]);

    const sortedSeats = seats.slice().sort((a, b) => {
        const seatNumberA = parseInt(a.seatNumber.replace(/^\D+/g, ''), 10);
        const seatNumberB = parseInt(b.seatNumber.replace(/^\D+/g, ''), 10);
        return seatNumberA - seatNumberB;
    });

    return (
        <div style={containerStyle}>
            <div style={headerStyle}>
                <h1>{`${name} - Section ${area} Seat Reservation`}</h1>
            </div>
            <div style={mainStyle}>
                <div style={seatContainerStyle}>
                    <div style={seatStyle}>
                        {sortedSeats.map((seat) => (
                            <button
                                key={seat.seatNumber}
                                style={seatButtonStyle}
                                onClick={() => handleSeatClick(seat.seatNumber, seat.occupied, seat.feedback,seat.status==='Confirmed')}
                            >
                                <span>{`F${index}${area}${seat.seatNumber.padStart(3, '0')}`}</span>
                                <span style={{ color: seat.occupied ? 'red' : seat.status === 'Confirmed' ? '#a60924' : seat.status === 'Reported' ? '#ecc334' : '#4cac34', fontSize: '30px' }}>●</span>
                            </button>
                        ))}
                    </div>
                </div>
                <div style={imageContainerStyle}>
                    <img src={getImage()} alt="Library Layout" style={imageStyle} />
                </div>
            </div>
            <button style={backButtonStyle} onClick={() => history.push(`/Student`)}>
                ↩
            </button>
            <SeatReservationModal
                isOpen={isModalOpen}
                library={name}
                floor={index.toString()}
                area={area}
                seat={selectedSeat || ''}
                occupied={isSeatOccupied}
                reported={isSeatReported}
                confirmed={isSeatConfirmed}
                onClose={handleCloseModal}
            />
            {seatError && <ErrorModal message={seatError} onClose={clearSeatError} />}
        </div>
    );
};

const containerStyle: React.CSSProperties = {
    padding: '20px',
    textAlign: 'center',
    position: 'relative',
    height: '95vh',
    display: 'flex',
    flexDirection: 'column',
    overflow: 'hidden',
};

const headerStyle: React.CSSProperties = {
    backgroundColor: 'purple',
    color: 'white',
    padding: '10px 20px',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
};

const seatInfoStyle: React.CSSProperties = {
    marginTop: '10px',
    display: 'flex',
    justifyContent: 'space-around',
    width: '100%',
};

const mainStyle: React.CSSProperties = {
    display: 'flex',
    flex: 1,
    overflow: 'hidden',
};

const seatContainerStyle: React.CSSProperties = {
    backgroundColor: '#e7e7e7',
    width: '30%',
    padding: '10px',
    display: 'flex',
    flexDirection: 'column',
    marginTop:'5px',
    marginBottom: '10px',
    border: '1px solid #ccc', // 添加边框
    boxShadow: '4px 4px 8px rgba(0, 0, 0, 0.1)', // 添加阴影
};

const seatStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    overflowY: 'auto',
    maxHeight: 'calc(100vh - 80px)',
};

const seatButtonStyle: React.CSSProperties = {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: '10px 10px',
    backgroundColor: 'white',
    border: '1px solid #ddd',
    borderRadius: '5px',
    marginBottom: '1px',
    cursor: 'pointer',
    width: '100%',
    height: '30px',
};

const imageContainerStyle: React.CSSProperties = {
    position: 'relative',
    width: '65%',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
};

const imageStyle: React.CSSProperties = {
    maxWidth: '100%',
    maxHeight: '100%',
};

const backButtonStyle: React.CSSProperties = {
    position: 'absolute',
    bottom: '20px',
    right: '20px',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '50%',
    width: '50px',
    height: '50px',
    fontSize: '24px',
    cursor: 'pointer',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: 2,
};