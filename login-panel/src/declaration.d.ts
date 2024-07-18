declare module '*.svg' {
    const content: any
    export default content
}

declare module '*.png' {
    const content: any
    export default content
}

declare module '*.jpg' {
    const content: any
    export default content
}

declare module 'react-star-ratings'

declare module '*.less' {
    const content: { [className: string]: string }
    export default content
}

declare module '*.css' {
    const content: { [className: string]: string }
    export = content
}

declare module '*.scss' {
    const content: { [className: string]: string }
    export = content
}

declare module '*.ttf' {
    const content: { [className: string]: string }
    export = content
}

declare interface NodeRequire {
    context: (directory: string, useSubdirectories: boolean, regExp: RegExp) => {
        keys: () => string[];
        (id: string): any;
    };
}

declare namespace __WebpackModuleApi {
    interface RequireContext {
        keys(): string[];
        (id: string): any;
    }
}