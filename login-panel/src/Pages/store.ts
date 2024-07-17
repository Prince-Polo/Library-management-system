import {create} from 'zustand';

export type Info = {
    valid: boolean;
    userName: string;
    token: string;
    volunteerStatus: string;
    floor: string;
    sectionNumber: string;
    seatNumber: string;
    violationCount: number;
    volunteerHours: number;
};

type StoreState = {
    info: Info | null;
    setInfo: (info: Info) => void;
    clearInfo: () => void;
};

export const useStore = create<StoreState>((set) => ({
    info: null,
    setInfo: (info) => set({ info }),
    clearInfo: () => set({ info: null }),
}));

interface AppState {
    Keys: string;
    setKeys: (newString: string) => void;
}

export const useKeys = create<AppState>((set) => ({
    Keys: '',
    setKeys: (newString: string) => set({ Keys: newString }),
}));