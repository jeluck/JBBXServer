 package com.jbb.mgt.core.service;

import com.jbb.mgt.core.domain.IdCard;

public interface FaceService {

     IdCard faceOCR(int userId, byte[] photoContent);
}
