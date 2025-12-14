import type { SwipeResponse } from "./types";

const BASE_URL = "http://localhost:8083/swipe-service/api/v1/swipes";

export async function sendSwipeRequest(
    userA: string,
    userB: string,
    token: string
): Promise<SwipeResponse> {
    const response = await fetch(`${BASE_URL}/${userA}/${userB}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ dir: "like" }),
    });

    const data = await response.json().catch(() => ({}));

    if (!response.ok) {
        return {
            message: `Error ${response.status}: ${response.statusText}`,
            ...data,
        };
    }

    return data;
}

export async function checkMatchRequest(
    userA: string,
    userB: string,
    token: string
): Promise<SwipeResponse> {
    const response = await fetch(`${BASE_URL}/matches/${userA}/${userB}`, {
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
            ...data,
        };
    }

    return data;
}
