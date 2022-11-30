declare const VERSION: string;
declare const SERVER_API_URL: string;
declare const DEVELOPMENT: string;

declare module '*.json' {
  const value: any;
  export default value;
}

declare module '*.html' {
  const value: string;
  export default value;
}

declare module '*.png';
declare module '*.jpg';
declare module '*.webp';
