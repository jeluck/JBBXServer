package com.jbb.server.core.service;

import com.jbb.server.core.domain.LiveDetectForeResult;

public interface TencentService {
    String sign();

    LiveDetectForeResult liveDetectFour(int userId, String validateData, byte[] videoContent);

    String liveGetFour();
}
