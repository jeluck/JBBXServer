 package com.jbb.third.aliyun.api;

 
 /*
 * Copyright 2017 Alibaba Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 import com.alibaba.cloudapi.sdk.core.BaseApiClient;
import com.alibaba.cloudapi.sdk.core.BaseApiClientBuilder;
import com.alibaba.cloudapi.sdk.core.annotation.NotThreadSafe;
import com.alibaba.cloudapi.sdk.core.annotation.ThreadSafe;
import com.alibaba.cloudapi.sdk.core.enums.Method;
import com.alibaba.cloudapi.sdk.core.enums.Scheme;
import com.alibaba.cloudapi.sdk.core.model.ApiRequest;
import com.alibaba.cloudapi.sdk.core.model.ApiResponse;
import com.alibaba.cloudapi.sdk.core.model.BuilderParams;

 @ThreadSafe
 public final class SyncApiClientOCRIdCard extends BaseApiClient {
     public final static String GROUP_HOST = "dm-51.data.aliyun.com";

     private SyncApiClientOCRIdCard(BuilderParams builderParams) {
         super(builderParams);
     }

     @NotThreadSafe
     public static class Builder extends BaseApiClientBuilder<SyncApiClientOCRIdCard.Builder, SyncApiClientOCRIdCard>{

         @Override
         protected SyncApiClientOCRIdCard build(BuilderParams params) {
             return new SyncApiClientOCRIdCard(params);
         }
     }

     public static Builder newBuilder(){
         return new SyncApiClientOCRIdCard.Builder();
     }

     public static SyncApiClientOCRIdCard getInstance(){
         return getApiClassInstance(SyncApiClientOCRIdCard.class);
     }

     public ApiResponse ocrIdCard(byte[] _body) {
         String _apiPath = "/rest/160601/ocr/ocr_idcard.json";

         ApiRequest _apiRequest = new ApiRequest(Scheme.HTTP, Method.POST_BODY, GROUP_HOST, _apiPath, _body);


         return syncInvoke(_apiRequest);
     }

 }