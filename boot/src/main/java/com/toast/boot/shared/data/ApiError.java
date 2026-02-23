/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.shared.data;

import java.util.List;

public record ApiError(
        List<ApiErrorDetail> details,
        String message,
        String timestamp
) {
    public record ApiErrorDetail(
            String code,
            String field,
            String message
    ) {
    }
}
