import { useState,useCallback } from 'react';
import { useHistory } from 'react-router';
import { sendPostRequest } from 'Pages/ErrorMessage';
import { useStore, useKeys } from 'Pages/store';
import { StudentInfoUsingTokenMessage } from 'Plugins/StudentAPI/StudentInfoUsingTokenMessage';
import {QueryAllFloorsMessage} from 'Plugins/SeatAPI/QueryAllFloorsMessage'
import { QuerySectionsByFloorMessage } from 'Plugins/SeatAPI/QuerySectionsByFloorMessage';
import {QuerySeatsInSectionMessage} from 'Plugins/SeatAPI/QuerySeatsInSectionMessage';
import axios from 'axios'


export const useToMyCenter = () => {
    const [error, setError] = useState<string | null>(null);
    const history = useHistory();
    const info = useStore((state) => state.info);
    const setInfo = useStore((state) => state.setInfo);
    const token = info.token;
    const clearToMyCenterError=()=>{
        setError(null);
    }
    const toMyCenter = () => {
        const message = new StudentInfoUsingTokenMessage(token);
        sendPostRequest(message, setError, (response) => {
            console.log("Student Info:", response);
            try {
                const parsedResponse = JSON.parse(response);
                console.log("Result:", parsedResponse);
                if ("error" in parsedResponse && parsedResponse["error"] != "") {
                    setError(parsedResponse.error);
                    console.log(parsedResponse);
                    setError(error);
                    setTimeout(() => history.push('/'), 3000);
                } else {
                    setInfo({valid: true,
                        userName: parsedResponse.userName,
                        token: info.token,
                        volunteerStatus: parsedResponse.volunteerStatus,
                        floor: parsedResponse.floor,
                        sectionNumber: parsedResponse.sectionNumber,
                        seatNumber: parsedResponse.seatNumber,
                        violationCount: parsedResponse.violationCount,
                        volunteerHours: parsedResponse.volunteerHours
                    });
                    history.push("/mycenter");
                }
            }catch (error) {
                setError(error);
                setTimeout(() => history.push('/'), 3000);
            }
        });
    };
    const err=error;
    return { toMyCenter, err, clearToMyCenterError };
};


export interface FloorInfo {
    floor: string;
    sections: string;
    totalSeats: string;
    freeSeats: string;
}

export interface FloorData {
    floorCount: string;
    floors: FloorInfo[];
}

export const useFloorData = () => {
    const [floorData, setFloorData] = useState<FloorData | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [hasFetched,setHasFetched] = useState(false);

    const fetchFloorData = useCallback(() => {
        if (!hasFetched) {
            const message = new QueryAllFloorsMessage();
            sendPostRequest(message, setError, (response) => {
                const parsedResponse = JSON.parse(response);
                setFloorData(parsedResponse);
                setHasFetched(true);
            });
        }
    }, [hasFetched]);

    const clearError = () => {
        setError(null);
    };

    return { floorData, fetchFloorData, error, clearError };
};

export interface SectionInfo {
    section: string;
    totalSeats: string;
    freeSeats: string;
}

export interface SeatInfo {
    floor: string;
    section: string;
    seatNumber: string;
    status: string;
    feedback: string;
    occupied: string;
    studentNumber: string;
}

export const useSectionData = () => {
    const [sections, setSections] = useState<SectionInfo[]>([]);
    const [error, setError] = useState<string | null>(null);

    const fetchSections = useCallback(async(floor: string) => {
        const message = new QuerySectionsByFloorMessage(floor);
        sendPostRequest(message, setError, (response) => {
            const parsedResponse = JSON.parse(response);
            setSections(parsedResponse.sections);
        });
    }, []);

    const clearError = useCallback(() => {
        setError(null);
    }, []);

    return { sections, fetchSections, error, clearError };
};

export const useSeatData = () => {
    const [seats, setSeats] = useState<SeatInfo[]>([]);
    const [error, setError] = useState<string | null>(null);

    const fetchSeats = useCallback( (floor: string, section: string) => {
        const message = new QuerySeatsInSectionMessage(floor, section);
        sendPostRequest(message, setError, (response) => {
            const parsedResponse = JSON.parse(response);
            setSeats(parsedResponse.seats);
        });
    }, []);

    const clearError = useCallback(() => {
        setError(null);
    }, []);

    return { seats, fetchSeats, error, clearError };
};
