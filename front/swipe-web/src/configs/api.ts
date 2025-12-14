import type { ConfigPayload, ConfigResponse } from "./types";

const BASE_URL = "http://localhost:8083/confg-ms/api/v1/config";

export async function postConfig(
    userId: string,
    payload: ConfigPayload,
    token: string
): Promise<ConfigResponse | { message: string }> {
    const response = await fetch(`${BASE_URL}/${userId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
    });

    const data = await response.json().catch(() => ({}));

    if (!response.ok) {
        return {
            message: `Error ${response.status}: ${response.statusText}`,
            ...(data as any),
        };
    }

    return data as ConfigResponse;
}

export async function getConfigApi(
    userId: string,
    token: string
): Promise<ConfigResponse | { message: string }> {
    const response = await fetch(`${BASE_URL}/${userId}`, {
        method: "GET",
        headers: {
            Accept: "application/json",
            Authorization: `Bearer ${token}`,
        },
    });

    const data = await response.json().catch(() => ({}));

    if (!response.ok) {
        return {
            message: `Error ${response.status}: ${response.statusText}`,
            ...(data as any),
        };
    }

    return data as ConfigResponse;
}
