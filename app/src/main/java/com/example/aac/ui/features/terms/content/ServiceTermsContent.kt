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
fun ServiceTermsContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        /* ---------- 제1조 ---------- */
        TermsArticleTitle("제1조 (목적)")
        TermsParagraph(
            "본 약관은 '모두와 AAC'(이하 '서비스')가 제공하는 AI 기반 의사소통 보조 서비스 및 " +
                    "관련 제반 서비스의 이용과 관련하여, 서비스와 회원 간의 권리, 의무 및 책임 사항, " +
                    "기타 필요한 사항을 규정함을 목적으로 합니다."
        )

        /* ---------- 제2조 ---------- */
        TermsArticleTitle("제2조 (용어의 정의)")

        TermsNumberedDefinition(
            1,
            "서비스",
            "회사가 구현한 AI 문장 추천, 낱말 카드 시스템, TTS 출력 등 의사소통 보조 소프트웨어를 의미합니다."
        )
        TermsNumberedDefinition(
            2,
            "회원",
            "본 약관에 동의하고 서비스를 이용하는 모든 사용자를 의미합니다."
        )
        TermsNumberedDefinition(
            3,
            "낱말 카드/키트",
            "사용자가 서비스를 통해 생성하거나 커뮤니티에서 다운로드한 의사소통 단어 집합을 의미합니다."
        )
        TermsNumberedDefinition(
            4,
            "AI 학습 데이터",
            "사용자가 선택하거나 수정한 문장 정보를 통해 AI 모델의 정확도를 높이는 데 사용되는 비식별 데이터를 의미합니다."
        )

        /* ---------- 제3조 ---------- */
        TermsArticleTitle("제3조 (서비스의 제공 및 변경)")

        TermsNumberedParagraph(
            1,
            "서비스는 다음과 같은 기능을 제공합니다."
        )
        TermsDotBullet("낱말 카드를 활용한 문장 조합 및 TTS 출력")
        TermsDotBullet("AI 기반 실시간 문장 추천 (AI-01, AI-05 등)")
        TermsDotBullet("개인 맞춤형 일과 루틴 관리")
        TermsDotBullet("외부 입력 장치(시선 추적기 등) 연동 지원")

        TermsNumberedParagraph(
            2,
            "서비스는 기술적 사양의 변경이나 AI 모델 고도화를 위해 서비스 내용을 변경할 수 있으며, 이 경우 앱 내 공지사항을 통해 사전 고지합니다."
        )

        /* ---------- 제4조 ---------- */
        TermsArticleTitle("제4조 (AI 문장 생성에 관한 특칙)")

        TermsNumberedDefinition(
            1,
            "생성 결과의 책임",
            "AI가 생성한 추천 문장은 모델의 확률적 결과물이며, 사용자는 재생 전 해당 문장이 자신의 의도와 일치하는지 확인해야 합니다. " +
                    "서비스는 AI 생성 문장으로 인해 발생한 오해나 사회적 갈등에 대해 직접적인 책임을 지지 않습니다."
        )

        TermsNumberedDefinition(
            2,
            "데이터 활용",
            "서비스는 문장 추천 품질 향상을 위해 사용자가 선택한 문장 데이터를 수집할 수 있습니다. " +
                    "단, 모든 데이터는 특정 개인을 식별할 수 없는 형태로 비식별화하여 처리합니다."
        )

        /* ---------- 제5조 ---------- */
        TermsArticleTitle("제5조 (사용자의 의무)")

        TermsNumberedParagraph(
            1,
            "회원은 다음 행위를 해서는 안 됩니다."
        )
        TermsDotBullet("타인의 계정을 도용하거나 부정하게 사용하는 행위")
        TermsDotBullet("서비스의 AI 모델을 역설계(Reverse Engineering)하거나 부당하게 추출하려는 행위")
        TermsDotBullet("커뮤니티에 음란, 폭력, 비하적 내용이 담긴 낱말 키트를 공유하는 행위")

        TermsNumberedParagraph(
            2,
            "회원은 자신의 신체적 조건에 맞는 입력 장치(시선 추적기 등) 설정값과 안전 수칙을 준수해야 합니다."
        )

        /* ---------- 제6조 ---------- */
        TermsArticleTitle("제6조 (유료 서비스 및 환불)")

        TermsNumberedParagraph(
            1,
            "서비스는 일부 고급 기능(AI 무제한 추천, 데이터 클라우드 백업 등)을 유료 구독 형태로 제공할 수 있습니다."
        )
        TermsNumberedParagraph(
            2,
            "결제 및 환불은 각 앱 스토어(Google Play Store 등)의 정책 및 관련 법령을 따릅니다."
        )

        /* ---------- 제7조 ---------- */
        TermsArticleTitle("제7조 (서비스 중단 및 면책)")

        TermsNumberedParagraph(
            1,
            "서비스는 천재지변, 서버 점검, 네트워크 장애 등 불가항력적인 사유가 발생한 경우 서비스 제공을 일시적으로 중단할 수 있습니다."
        )
        TermsNumberedParagraph(
            2,
            "AI 문장 추천 기능은 인터넷 연결이 필수적이며, 네트워크 환경에 따른 서비스 지연이나 불능에 대해 서비스는 책임을 지지 않습니다."
        )

        /* ---------- 제8조 ---------- */
        TermsArticleTitle("제8조 (개인정보 보호)")
        TermsParagraph(
            "서비스는 회원의 개인정보를 보호하기 위해 관련 법령을 준수하며, 상세한 사항은 '개인정보 처리방침'에 따릅니다."
        )
    }
}

/* ================= 공통 컴포넌트 ================= */

@Composable
fun TermsArticleTitle(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black
    )
}

@Composable
fun TermsParagraph(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        color = Color(0xFF494949)
    )
}

@Composable
fun TermsNumberedParagraph(
    number: Int,
    text: String
) {
    Text(
        text = "$number. $text",
        fontSize = 20.sp,
        lineHeight = 28.sp,
        color = Color(0xFF494949)
    )
}

@Composable
fun TermsNumberedDefinition(
    number: Int,
    title: String,
    description: String
) {
    Text(
        text = "$number. $title: $description",
        fontSize = 20.sp,
        lineHeight = 28.sp,
        color = Color(0xFF494949)
    )
}

@Composable
fun TermsDotBullet(text: String) {
    Text(
        text = "· $text",
        fontSize = 20.sp,
        lineHeight = 28.sp,
        color = Color(0xFF494949),
        modifier = Modifier.padding(start = 16.dp)
    )
}

@Composable
fun TermsPlainParagraph(
    text: String
) {
    Text(
        text = text,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        color = Color(0xFF494949)
    )
}
