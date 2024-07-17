import {create} from "zustand"

export type LibraryInfo = {
    name: string;
    index: number;
    total: number;
    available: number;
};

type StoreLibState = {
    libraryInfo:LibraryInfo | null;
    setLibraryInfo: (libraryInfo: LibraryInfo) => void;
    clearLibraryInfo: () => void;
};

export const useStoreLib = create<StoreLibState>((set) => ({
    libraryInfo: null,
    setLibraryInfo: (libraryInfo) => set({ libraryInfo }),
    clearLibraryInfo: () => set({ libraryInfo: null }),
}));

export type Selected = {
    library: string;
    floor: string;
    area: string;
    seat: string;
};
type StoreSelected = {
    library:Selected | null;
    setLibrary: (library: Selected) => void;
    clearLibrary: () => void;
};

export const useStoreSelected = create<StoreSelected>((set) => ({
    library: null,
    setLibrary: (library) => set({ library }),
    clearLibrary: () => set({ library: null }),
}));

