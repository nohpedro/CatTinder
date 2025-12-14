export type ConfigPayload = {
    userid: string;
    darkmode: boolean;
    showInfo: boolean;
    showOnlineStatus: boolean;
    showNotifications: boolean;
};

export type ConfigResponse = ConfigPayload & {
    message?: string;
};
