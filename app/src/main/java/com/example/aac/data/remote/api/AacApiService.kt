package com.example.aac.data.remote.api

import com.example.aac.data.remote.dto.*
import retrofit2.http.*

interface AacApiService {

    // ----------------------------------------------------
    // [Auth]
    // ----------------------------------------------------

    // [Auth] 게스트 로그인
    @POST("api/auth/guest")
    suspend fun createGuestAccount(
        @Body request: GuestLoginRequest
    ): GuestLoginResponse

    // [Auth] 내 정보 조회
    @GET("api/auth/me")
    suspend fun getMyInfo(): MyInfoResponse

    // [Auth] 로그아웃
    @POST("api/auth/logout")
    suspend fun logout(): LogoutResponse

    // [Auth] 회원탈퇴
    @DELETE("api/auth/account")
    suspend fun withdraw(): BaseResponse<Unit>

    // [Auth] 카카오 SDK 로그인
    @POST("api/auth/kakao/sdk")
    suspend fun kakaoLogin(
        @Body request: KakaoLoginRequest
    ): KakaoLoginResponse

    // [Auth] 약관 동의
    @POST("api/auth/social/complete")
    suspend fun completeSocialSignup(
        @Body request: SocialCompleteRequest
    ): KakaoLoginResponse

    // [Auth] 약관 목록 조회
    @GET("api/auth/terms")
    suspend fun getTerms(): BaseResponse<List<TermsResponse>>


    // ----------------------------------------------------
    // [Main]
    // ----------------------------------------------------

    // [Main] 단어 목록 조회
    @GET("api/words")
    suspend fun getWords(
        @Query("categoryId") categoryId: String? = null,
        @Query("onlyFavorite") onlyFavorite: Boolean? = null
    ): WordResponse


    // ----------------------------------------------------
    // [Category]
    // ----------------------------------------------------

    // [Category] 카테고리 목록 조회
    @GET("api/categories")
    suspend fun getCategories(): BaseResponse<List<CategoryResponse>>

    // [Category] 카테고리 생성
    @POST("api/categories")
    suspend fun createCategory(
        @Body request: CreateCategoryRequest
    ): BaseResponse<CategoryResponse>

    // [Category] 카테고리 수정
    @PATCH("api/categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: String,
        @Body request: UpdateCategoryRequest
    ): BaseResponse<CategoryResponse>

    // [Category] 카테고리 삭제
    @DELETE("api/categories/{id}")
    suspend fun deleteCategory(
        @Path("id") id: String
    ): BaseResponse<DeleteCategoryResponse>

    // [Category] 카테고리 순서 변경
    @PATCH("api/order/categories")
    suspend fun updateCategoryOrders(
        @Body request: CategoryOrderRequest
    ): BaseResponse<CategoryResponse>


    // ----------------------------------------------------
    // [Setting]
    // ----------------------------------------------------

    // [Setting] 그리드 설정 조회
    @GET("api/settings/grid")
    suspend fun getGridSetting(): GridSettingResponse

    // [Setting] 그리드 설정 수정
    @PATCH("api/settings/grid")
    suspend fun updateGridSetting(
        @Body request: GridSettingRequest
    ): GridSettingResponse


    // ----------------------------------------------------
    // [AI]
    // ----------------------------------------------------

    // [AI] 문장 추천
    @POST("api/ai/predictions")
    suspend fun getAiPredictions(
        @Body request: AiPredictionRequest
    ): AiPredictionResponse


    // ----------------------------------------------------
    // [Routine - 자동 출력 문장]
    // ----------------------------------------------------

    // [Routine] 목록 조회
    @GET("api/routines")
    suspend fun getRoutines(): BaseResponse<RoutinesDataDto>

    // [Routine] 생성
    @POST("api/routines")
    suspend fun createRoutine(
        @Body request: CreateRoutineRequest
    ): BaseResponse<RoutineDto>

    // [Routine] 수정
    @PATCH("api/routines/{id}")
    suspend fun updateRoutine(
        @Path("id") id: String,
        @Body body: RoutineUpdateRequest
    ): RoutineUpdateResponse

    // [Routine] 선택 삭제
    @HTTP(method = "DELETE", path = "api/routines", hasBody = true)
    suspend fun deleteRoutines(
        @Body body: DeleteRoutinesRequest
    ): BaseResponse<DeleteRoutinesResponse>

    // [Routine] 전체 삭제
    @DELETE("api/routines/all")
    suspend fun deleteAllRoutines(): BaseResponse<DeleteAllRoutinesResponse>

}
