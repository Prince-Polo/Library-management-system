import {create} from 'zustand';

export type Info = {
    valid: boolean;
    userName: string;
    token: string;
    volunteerStatus: boolean;
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
    volunteerModal: boolean;
    setVolunteerModal: (value: boolean) => void;
    clearVolunteerModal: () => void;
};

export const useStore = create<StoreState>((set) => ({
    info: null,
    setInfo: (info) => set({ info }),
    clearInfo: () => set({ info: null }),
    volunteerModal: false,
    setVolunteerModal: (value) => set({ volunteerModal: value }),
    clearVolunteerModal: () => set({ volunteerModal: false }),
}));

type BooleanState = {
    value: boolean;
    setValue: (value: boolean) => void;
    toggleValue: () => void;
};

export const useBooleanStore = create<BooleanState>((set) => ({
    value: false,
    setValue: (value) => set({ value }),
    toggleValue: () => set((state) => ({ value: !state.value })),
}));