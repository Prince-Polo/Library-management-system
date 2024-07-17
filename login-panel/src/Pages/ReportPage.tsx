import React, { useState } from 'react';
import { useHistory } from 'react-router';
import { sendPostRequest, ErrorModal, SuccessModal } from 'Pages/ErrorMessage';
import { useStoreSelected } from 'Pages/LibrarySetting';
import { SeatReportMessage } from 'Plugins/SeatAPI/SeatReportMessage';

export const ReportIssue: React.FC = () => {
    const history = useHistory();
    const selected = useStoreSelected((state) => state.library);
    const { library, floor, area, seat } = selected;
    const [checkedOptions, setCheckedOptions] = useState<{ [key: string]: boolean }>({
        seatDamage: false,
        deskDamage: false,
        electricalIssue: false,
        other: false,
    });

    const [otherIssueText, setOtherIssueText] = useState('');
    const [deskText, setDeskText] = useState('');
    const [seatText, setSeatText] = useState('');
    const [electricalText, setElectricalText] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const checkString=(inputString:string)=>{
        return inputString.charCodeAt(0)-48;
    }
    const floornum=checkString(floor);

    const handleCheckboxChange = (option: string) => {
        setCheckedOptions(prevState => ({ ...prevState, [option]: !prevState[option] }));
    };

    const handleTextareaChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setOtherIssueText(e.target.value);
    };

    const handleSeatChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setSeatText(e.target.value);
    }

    const handleDeskChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setDeskText(e.target.value);
    }

    const handleElectricalChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setElectricalText(e.target.value);
    }

    const handleSubmit = () => {
        const reportData = `At ${library}, floor ${floor === "0" ? "G" : floor} area ${area}, seat ${floor === "0" ? "G" : floor}${area}${seat}, there are the following issues:${checkedOptions.seatDamage ? ` Seat damages: ${seatText}` : ""}${checkedOptions.deskDamage ? ` Desk damages: ${deskText}` : ""}${checkedOptions.electricalIssue ? ` Electrical issues: ${electricalText}` : ""}${checkedOptions.other ? ` Other issues: ${otherIssueText}` : ''}`;
        const message = new SeatReportMessage(floor, area, seat, reportData);

        console.log(floornum);
        sendPostRequest(message, setError, (response) => {
            setSuccess('Feedback submitted successfully!');
            setTimeout(() => {
                setSuccess(null);
                history.push('/Student')
            }, 2000);
        });
    };

    const closeModal = () => {
        setError(null);
    };

    const closeSuccessModal = () => {
        setSuccess(null);
    };

    return (
        <div style={containerStyle}>
            <div style={headerStyle}>
                <button style={backButtonStyle} onClick={() => history.push('/Student')}>
                    &lt;
                </button>
                <h2>Report an Issue</h2>
            </div>
            <h3 style={titleStyle}>Report an issue for seat {seat} in area {area} on floor {floor} of {library} library</h3>
            <div style={checkboxContainerStyle}>
                <div style={inputContainerStyle}>
                    <label style={inputLabelStyle}>
                        <span style={checkboxLabelStyle}>Seat Damage</span>
                        <input type="checkbox" checked={checkedOptions.seatDamage} onChange={() => handleCheckboxChange('seatDamage')} />
                    </label>
                    {checkedOptions.seatDamage && (
                        <textarea
                            style={{ ...textareaStyle, display: 'block', resize: 'vertical' }}
                            rows={5}
                            value={seatText}
                            onChange={handleSeatChange}
                        />
                    )}
                </div>
                <div style={inputContainerStyle}>
                    <label style={inputLabelStyle}>
                        <span style={checkboxLabelStyle}>Desk Damage</span>
                        <input type="checkbox" checked={checkedOptions.deskDamage} onChange={() => handleCheckboxChange('deskDamage')} />
                    </label>
                    {checkedOptions.deskDamage && (
                        <textarea
                            style={{ ...textareaStyle, display: 'block', resize: 'vertical' }}
                            rows={5}
                            value={deskText}
                            onChange={handleDeskChange}
                        />
                    )}
                </div>
                <div style={inputContainerStyle}>
                    <label style={inputLabelStyle}>
                        <span style={checkboxLabelStyle}>Electrical Issue</span>
                        <input type="checkbox" checked={checkedOptions.electricalIssue} onChange={() => handleCheckboxChange('electricalIssue')} />
                    </label>
                    {checkedOptions.electricalIssue && (
                        <textarea
                            style={{ ...textareaStyle, display: 'block', resize: 'vertical' }}
                            rows={5}
                            value={electricalText}
                            onChange={handleElectricalChange}
                        />
                    )}
                </div>
                <div style={inputContainerStyle}>
                    <label style={inputLabelStyle}>
                        <span style={checkboxLabelStyle}>Other</span>
                        <input type="checkbox" checked={checkedOptions.other} onChange={() => handleCheckboxChange('other')} />
                    </label>
                    {checkedOptions.other && (
                        <textarea
                            style={{ ...textareaStyle, display: 'block', resize: 'vertical' }}
                            rows={5}
                            value={otherIssueText}
                            onChange={handleTextareaChange}
                        />
                    )}
                </div>
            </div>
            <button style={submitButtonStyle} onClick={handleSubmit}>
                Submit
            </button>
            <ErrorModal message={error} onClose={closeModal} />
            <SuccessModal message={success} onClose={closeSuccessModal} />
        </div>
    );
};

const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    padding: '20px',
    alignItems: 'center',
    width: '100%',
};

const headerStyle: React.CSSProperties = {
    textAlign: 'center',
    backgroundColor: 'purple',
    color: 'white',
    padding: '10px',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    fontSize: '20px',
    width: '100%',
};

const backButtonStyle: React.CSSProperties = {
    backgroundColor: 'transparent',
    border: 'none',
    color: 'white',
    fontSize: '24px',
    cursor: 'pointer',
};

const titleStyle: React.CSSProperties = {
    textAlign: 'center',
    margin: '20px 0',
    fontSize: '24px',
};

const checkboxContainerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    marginBottom: '20px',
    fontSize: '24px',
    width: '100%',
};

const textareaStyle: React.CSSProperties = {
    width: '100%',
    padding: '10px',
    margin: '10px 0',
    fontSize: '18px',
    display: 'none', // Initially hidden
};

const submitButtonStyle: React.CSSProperties = {
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    padding: '10px 20px',
    cursor: 'pointer',
    alignSelf: 'flex-end',
};

const inputContainerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'flex-end',
    width: '100%',
    marginLeft:'10px',
    marginRight:'auto',
    marginBottom: '10px', // Added margin to separate the checkboxes
};

const inputLabelStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    fontSize: '16px', // Adjust the font size for the input labels
    width: '100%',
    justifyContent: 'space-between', // Space out the label and the checkbox
};

const checkboxLabelStyle: React.CSSProperties = {
    marginRight: '10px',
    whiteSpace: 'nowrap', // Ensure the text does not wrap to a new line
};
