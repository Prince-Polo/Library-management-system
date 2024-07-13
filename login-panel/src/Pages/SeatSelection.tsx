import React from 'react';
import { useParams } from 'react-router-dom';

interface Params {
    library: string;
}

const SeatSelection: React.FC = () => {
    const { library } = useParams<Params>();

    const seatSelectionStyle: React.CSSProperties = {
        textAlign: 'center',
    };

    return (
        <div style={seatSelectionStyle}>
            <h1>Seat Selection for Library {library}</h1>
            {/* Implement seat selection logic here */}
        </div>
    );
};

export default SeatSelection;