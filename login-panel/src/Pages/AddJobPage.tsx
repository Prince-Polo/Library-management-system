import React, { useState,useEffect } from 'react';
import { AddJobMessage } from 'Plugins/JobAPI/AddJobMessage';
import { sendPostRequest, ErrorModal, SuccessModal } from 'Pages/ErrorMessage';
import AdminLayout from './AdminLayout';

const AddJobPage: React.FC = () => {
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const [showConfirmModal, setShowConfirmModal] = useState<boolean>(false);
    const [jobData, setJobData] = useState<{
        jobShortDescription: string;
        jobLongDescription: string;
        jobHardness: number;
        jobCredit: number;
        jobRequired: number;
    }>({
        jobShortDescription: '',
        jobLongDescription: '',
        jobHardness: null,
        jobCredit: null,
        jobRequired: null
    });

    useEffect(() => {
        // Reset the form inputs when jobData changes
        console.log(success);
            setJobData({
                jobShortDescription: "",
                jobLongDescription: "",
                jobHardness: 0,
                jobCredit: 0,
                jobRequired: 0
            });
    }, []);

    const handleJobSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);

        // const form = e.target as HTMLFormElement;
        // const formData = new FormData(form);

        // const jobData = {
        //     jobShortDescription: formData.get('jobShortDescription') as string,
        //     jobLongDescription: formData.get('jobLongDescription') as string,
        //     jobHardness: parseInt(formData.get('jobHardness') as string),
        //     jobCredit: parseInt(formData.get('jobCredit') as string),
        //     jobRequired: parseInt(formData.get('jobRequired') as string)
        // };
        //
        // setJobData(jobData);
        setShowConfirmModal(true);
    };

    const handleConfirm = () => {
        if (!jobData) return;

        const message = new AddJobMessage(
            jobData.jobShortDescription,
            jobData.jobLongDescription,
            jobData.jobHardness.toString(),
            jobData.jobCredit.toString(),
            jobData.jobRequired.toString()
        );
        console.log(message);

        sendPostRequest(message, setError, () => {
            setSuccess("Job successfully added");
            setTimeout(() => setSuccess(null), 2000);
            setJobData({ jobShortDescription:"",jobLongDescription:"",jobCredit:0,jobHardness:0,jobRequired:0 });
        });
        setShowConfirmModal(false);
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setJobData({
            ...jobData,
            [name]: name === 'jobHardness' || name === 'jobCredit' || name === 'jobRequired' ? parseInt(value) : value
        });
    };

    return (
        <AdminLayout>
            <h2 style={headingStyle}>Add Job</h2>
            <form onSubmit={handleJobSubmit}>
                <div style={formGroupStyle}>
                    <label htmlFor="jobShortDescription" style={labelStyle}>Short Description:</label>
                    <input type="text" id="jobShortDescription" name="jobShortDescription" value={jobData.jobShortDescription} required style={inputStyle} onChange={handleInputChange} />
                </div>
                <div style={formGroupStyle}>
                    <label htmlFor="jobLongDescription" style={labelStyle}>Long Description:</label>
                    <input type="text" id="jobLongDescription" name="jobLongDescription" value={jobData.jobLongDescription} required style={inputStyle} onChange={handleInputChange} />
                </div>
                <div style={formGroupStyle}>
                    <label htmlFor="jobHardness" style={labelStyle}>Hardness:</label>
                    <input type="number" id="jobHardness" name="jobHardness" value={jobData.jobHardness} required style={inputStyle} min={1} max={10} step={1} onChange={handleInputChange} />
                </div>
                <div style={formGroupStyle}>
                    <label htmlFor="jobCredit" style={labelStyle}>Credit:</label>
                    <input type="number" id="jobCredit" name="jobCredit" value={jobData.jobCredit} required style={inputStyle} min={1} max={8} step={1} onChange={handleInputChange} />
                </div>
                <div style={formGroupStyle}>
                    <label htmlFor="jobRequired" style={labelStyle}>Required:</label>
                    <input type="number" id="jobRequired" name="jobRequired" value={jobData.jobRequired} required style={inputStyle} min={1} step={1} onChange={handleInputChange} />
                </div>
                <button type="submit" style={submitButtonStyle}>Submit Job</button>
            </form>
            {error && <p style={errorMessageStyle}>{error}</p>}
            {success && <SuccessModal message={success} onClose={() => setSuccess(null)} />}
            {showConfirmModal && (
                <div style={modalStyle}>
                    <div style={modalContentStyle}>
                        <h3>Confirm Job Details</h3>
                        <p><strong>Short Description:</strong> {jobData?.jobShortDescription}</p>
                        <p><strong>Long Description:</strong> {jobData?.jobLongDescription}</p>
                        <p><strong>Hardness:</strong> {jobData?.jobHardness}</p>
                        <p><strong>Credit:</strong> {jobData?.jobCredit}</p>
                        <p><strong>Required:</strong> {jobData?.jobRequired}</p>
                        <button onClick={handleConfirm} style={confirmButtonStyle}>Confirm</button>
                        <button onClick={() => setShowConfirmModal(false)} style={cancelButtonStyle}>Cancel</button>
                    </div>
                </div>
            )}
        </AdminLayout>
    );
};

const headingStyle: React.CSSProperties = {
    marginBottom: '20px',
    fontFamily: 'Segoe UI, Tahoma, Geneva, Verdana, sans-serif',
    color: '#333',
};

const formGroupStyle: React.CSSProperties = {
    marginBottom: '15px',
};

const labelStyle: React.CSSProperties = {
    display: 'block',
    marginBottom: '5px',
    fontWeight: 'bold',
    color: '#555',
};

const inputStyle: React.CSSProperties = {
    width: '100%',
    padding: '8px',
    border: '1px solid #ccc',
    borderRadius: '5px',
    boxSizing: 'border-box',
};

const submitButtonStyle: React.CSSProperties = {
    background: '#32cd32',
    color: 'white',
    border: 'none',
    padding: '10px 15px',
    borderRadius: '5px',
    cursor: 'pointer',
    fontSize: '16px',
    transition: 'background 0.3s',
};

const errorMessageStyle: React.CSSProperties = {
    color: 'red',
    fontWeight: 'bold',
};

const modalStyle: React.CSSProperties = {
    position: 'fixed',
    top: 0,
    left: 0,
    width: '100%',
    height: '100%',
    background: 'rgba(0, 0, 0, 0.5)',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
};

const modalContentStyle: React.CSSProperties = {
    background: 'white',
    padding: '20px',
    borderRadius: '10px',
    textAlign: 'center',
};

const confirmButtonStyle: React.CSSProperties = {
    background: '#32cd32',
    color: 'white',
    border: 'none',
    padding: '10px 15px',
    borderRadius: '5px',
    cursor: 'pointer',
    margin: '10px',
};

const cancelButtonStyle: React.CSSProperties = {
    background: '#ff6347',
    color: 'white',
    border: 'none',
    padding: '10px 15px',
    borderRadius: '5px',
    cursor: 'pointer',
    margin: '10px',
};

export default AddJobPage;
