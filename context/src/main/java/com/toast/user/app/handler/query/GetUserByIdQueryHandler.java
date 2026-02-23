/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.user.app.handler.query;

import com.toast.shared.app.QueryHandler;
import com.toast.user.app.finder.UserFinder;
import com.toast.user.app.request.query.GetUserByIdQuery;
import com.toast.user.app.response.UserInfoResponse;

public class GetUserByIdQueryHandler implements QueryHandler<GetUserByIdQuery, UserInfoResponse> {
    private final UserFinder finder;
    
    public GetUserByIdQueryHandler(UserFinder finder) {
        this.finder = finder;
    }
    
    @Override
    public UserInfoResponse handle(GetUserByIdQuery query) {
        return finder.find(query.userId());
    }
}