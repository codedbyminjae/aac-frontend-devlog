package com.example.aac.ui.features.terms

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrivacyTermsContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        /* ---------- 제1조 ---------- */
        TermsArticleTitle("제1조 (개인정보의 처리 목적)")

        TermsPlainParagraph(
            "'모두와 AAC'(이하 '서비스')는 다음의 목적을 위하여 개인정보를 처리합니다. 처리하고 있는 개인정보는 " +
                    "다음의 목적 이외의 용도로는 이용되지 않으며, 이용 목적이 변경되는 경우에는 별도의 동의를 받는 등 " +
                    "필요한 조치를 이행할 예정입니다."
        )

        TermsNumberedParagraph(1,"회원 가입 및 관리: 회원 가입 의사 확인, 본인 식별·인증, 회원자격 유지·관리, 서비스 부정 이용 방지")
        TermsNumberedParagraph(2,"서비스 제공 및 맞춤화: AI 문장 추천 알고리즘 고도화, 개인별 루틴 관리, TTS 목소리 설정 보존")
        TermsNumberedParagraph(3,"AI 모델 학습 및 개선: 사용자가 선택·수정한 문장의 비식별 처리를 통한 문장 생성 알고리즘 품질 향상")

        /* ---------- 제2조 ---------- */
        TermsArticleTitle("제2조 (처리하는 개인정보의 항목)")

        TermsPlainParagraph(
            "서비스는 이용자로부터 다음과 같은 개인정보를 수집합니다."
        )

        TermsNumberedParagraph(1,"필수 항목: 이메일 주소, 비밀번호, 닉네임, 로그 데이터, 기기 정보(OS 버전, 기기 모델명)")
        TermsNumberedParagraph(2,"선택 항목: 프로필 사진, TTS 목소리 선호도, 외부 입력 장치(시선 추적기 등) 설정값")
        TermsNumberedParagraph(3,"서비스 이용 과정에서 생성되는 정보: 사용자가 선택한 낱말 조합, 최종 출력 문장 데이터, 편집된 문장 이력")

        /* ---------- 제3조 ---------- */
        TermsArticleTitle("제3조 (개인정보의 보유 및 이용 기간)")

        TermsNumberedParagraph(
            1,
            "서비스는 회원 탈퇴 시까지 개인정보를 보유 및 이용합니다."
        )

        TermsNumberedParagraph(
            2,
            "단, 관련 법령의 규정에 의하여 보존할 필요가 있는 경우 해당 법령에서 정한 기간 동안 보관합니다."
        )

        TermsNumberedParagraph(
            3,
            "탈퇴한 회원의 데이터 중 비식별화된 문장 학습 데이터는 AI 모델의 일관성을 위해 파기하지 않고 " +
                    "통계적 목적으로 활용될 수 있습니다."
        )

        /* ---------- 제4조 ---------- */
        TermsArticleTitle("제4조 (개인정보의 제3자 제공)")

        TermsPlainParagraph(
            "서비스는 원칙적으로 이용자의 개인정보를 제3자에게 제공하지 않습니다. 다만, 아래의 경우에는 예외로 합니다."
        )

        TermsNumberedParagraph(1,"이용자가 사전에 동의한 경우")
        TermsNumberedParagraph(2,"법령의 규정에 의거하거나 수사 목적으로 수사기관의 요구가 있는 경우")

        /* ---------- 제5조 ---------- */
        TermsArticleTitle("제5조 (개인정보 처리의 위탁)")

        TermsPlainParagraph(
            "서비스는 원활한 서비스 제공을 위해 다음과 같이 개인정보 처리 업무를 위탁하고 있습니다."
        )

        TermsDotBullet("Google Firebase / AWS: 데이터 보관 및 서버 운영 (클라우드 서비스)")
        TermsDotBullet("OpenAI (API): 문장 생성 로직 처리를 위한 텍스트 데이터 전송 (개인 식별 정보 제거 후 전송)")

        /* ---------- 제6조 ---------- */
        TermsArticleTitle("제6조 (민감정보의 처리 제한)")

        TermsNumberedParagraph(1,
            "서비스는 사용자의 건강 상태나 장애 등급 등 민감정보를 직접적으로 수집하지 않습니다."
        )

        TermsNumberedParagraph(
            2,
            "다만 AAC 서비스 특성상 대화 내용에 건강 관련 정보가 포함될 수 있으며, 이는 전적으로 사용자의 의사에 따라 입력되는 데이터로 간주되어 철저히 암호화하여 관리됩니다."
        )

        /* ---------- 제7조 ---------- */
        TermsArticleTitle("제7조 (이용자의 권리와 행사 방법)")

        TermsPlainParagraph(
            "이용자는 언제든지 자신의 개인정보를 조회하거나 수정할 수 있으며, 회원 탈퇴를 통해 개인정보 주체로서의 권리를 행사할 수 있습니다."
        )
    }
}



