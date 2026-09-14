package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.StoreNavScreen
import com.example.data.ShiftEntity
import com.example.ui.theme.CanvasBackground
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CardSurfaceVariant
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.ChampagneGoldDark
import com.example.ui.theme.ChampagneGoldLight
import com.example.ui.theme.RoseGoldLight
import com.example.ui.theme.RoseGoldPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningOrange

@Composable
fun StoreDrawerContent(
    currentScreen: StoreNavScreen,
    activeShift: ShiftEntity?,
    lowStockCount: Int,
    isDarkMode: Boolean,
    alertsEnabled: Boolean,
    soundHapticEnabled: Boolean,
    onScreenSelected: (StoreNavScreen) -> Unit,
    onToggleDarkMode: () -> Unit,
    onToggleAlerts: (Boolean) -> Unit,
    onToggleSoundHaptic: (Boolean) -> Unit,
    onOpenScanner: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = CardSurface,
        drawerTonalElevation = 0.dp,
        modifier = Modifier
            .width(320.dp)
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // --- 1. Brand Header ---
            Surface(
                color = CardSurfaceVariant,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.5.dp, RoseGoldPrimary, RoundedCornerShape(14.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.he_store_logo),
                            contentDescription = "H&E Store Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "H&E Store",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "POS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ChampagneGoldDark,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ChampagneGoldLight)
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = "نقاط البيع وإدارة المتجر",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(if (activeShift?.isOpen == true) SuccessGreen else TextMuted)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (activeShift?.isOpen == true) "الوردية: ${activeShift.cashierName}" else "الوردية مغلقة",
                                fontSize = 11.sp,
                                color = if (activeShift?.isOpen == true) SuccessGreen else TextMuted,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // --- 2. Navigation Section ---
            Text(
                text = "شاشات النظام",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )

            DrawerNavItem(
                title = "الكاشير والمبيعات",
                icon = Icons.Default.PointOfSale,
                selected = currentScreen == StoreNavScreen.CASHIER,
                onClick = {
                    onScreenSelected(StoreNavScreen.CASHIER)
                    onCloseDrawer()
                },
                testTag = "drawer_nav_cashier"
            )

            DrawerNavItem(
                title = "المخزون والأصناف",
                icon = Icons.Default.Inventory2,
                badgeCount = if (lowStockCount > 0) lowStockCount else null,
                selected = currentScreen == StoreNavScreen.INVENTORY,
                onClick = {
                    onScreenSelected(StoreNavScreen.INVENTORY)
                    onCloseDrawer()
                },
                testTag = "drawer_nav_inventory"
            )

            DrawerNavItem(
                title = "الوردية والخزينة",
                icon = Icons.Default.Schedule,
                selected = currentScreen == StoreNavScreen.SHIFT,
                onClick = {
                    onScreenSelected(StoreNavScreen.SHIFT)
                    onCloseDrawer()
                },
                testTag = "drawer_nav_shift"
            )

            DrawerNavItem(
                title = "الإدارة والتقارير",
                icon = Icons.Default.Assessment,
                selected = currentScreen == StoreNavScreen.MANAGER,
                onClick = {
                    onScreenSelected(StoreNavScreen.MANAGER)
                    onCloseDrawer()
                },
                testTag = "drawer_nav_manager"
            )

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = CardBorder, thickness = 1.dp)
            Spacer(modifier = Modifier.height(14.dp))

            // --- 3. Settings Section (الإعدادات: التنبيهات، الدارك مود، الباركود) ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = null,
                    tint = RoseGoldPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "الإعدادات العامة",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Setting 1: Dark Mode Toggle
            DrawerSettingSwitchItem(
                icon = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                title = "الوضع الليلي والنهاري",
                subtitle = if (isDarkMode) "الوضع الليلي مفعل حالياً" else "الوضع الفاتح مفعل حالياً",
                checked = isDarkMode,
                onCheckedChange = { onToggleDarkMode() },
                testTag = "drawer_setting_dark_mode"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Setting 2: Low Stock Notifications
            DrawerSettingSwitchItem(
                icon = if (alertsEnabled) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                title = "تنبيهات نواقص المخزون",
                subtitle = if (alertsEnabled) "تنبيه فوري عند انخفاض الرصيد ($lowStockCount نواقص)" else "التنبيهات متوقفة",
                checked = alertsEnabled,
                onCheckedChange = onToggleAlerts,
                testTag = "drawer_setting_alerts"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Setting 3: Sound & Vibration Feedback
            DrawerSettingSwitchItem(
                icon = Icons.Default.VolumeUp,
                title = "صوت واهتزاز الماسح",
                subtitle = if (soundHapticEnabled) "صوت بيب واهتزاز عند قراءة الباركود" else "الوضع الصامت",
                checked = soundHapticEnabled,
                onCheckedChange = onToggleSoundHaptic,
                testTag = "drawer_setting_sound"
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Setting 4: Google Barcode Scanning Shortcut
            Surface(
                color = CardSurfaceVariant,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        onCloseDrawer()
                        onOpenScanner()
                    }
                    .testTag("drawer_open_google_scanner")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(RoseGoldLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.QrCodeScanner,
                                contentDescription = null,
                                tint = RoseGoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "ماسح باركود جوجل (Google ML Kit)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "فحص مباشر وسريع بالكاميرا",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Text(
                        text = "فتح ←",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoseGoldPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = CardBorder, thickness = 1.dp)
            Spacer(modifier = Modifier.height(14.dp))

            // --- 4. Future / Store Info Section (حاجات تانية بعدين) ---
            Text(
                text = "معلومات النظام والبيانات",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )

            Surface(
                color = CanvasBackground,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("قاعدة البيانات المحلية:", fontSize = 11.sp, color = TextSecondary)
                        Text("Room SQLite (أوفلاين)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("مكتبة قراءة الباركود:", fontSize = 11.sp, color = TextSecondary)
                        Text("Google ML Kit 17.3", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RoseGoldPrimary)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("إصدار البرنامج:", fontSize = 11.sp, color = TextSecondary)
                        Text("v2.1.0 Luxury POS", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DrawerNavItem(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    badgeCount: Int? = null,
    onClick: () -> Unit,
    testTag: String
) {
    NavigationDrawerItem(
        icon = {
            if (badgeCount != null && badgeCount > 0) {
                BadgedBox(badge = {
                    Badge(
                        containerColor = WarningOrange,
                        contentColor = Color.White
                    ) {
                        Text(text = badgeCount.toString(), fontSize = 10.sp)
                    }
                }) {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
                }
            } else {
                Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            }
        },
        label = {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
        },
        selected = selected,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = RoseGoldLight,
            selectedIconColor = RoseGoldPrimary,
            selectedTextColor = RoseGoldPrimary,
            unselectedContainerColor = Color.Transparent,
            unselectedIconColor = TextSecondary,
            unselectedTextColor = TextPrimary
        ),
        modifier = Modifier
            .padding(vertical = 2.dp)
            .testTag(testTag)
    )
}

@Composable
private fun DrawerSettingSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Surface(
        color = CardSurfaceVariant,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (checked) RoseGoldLight else CanvasBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = if (checked) RoseGoldPrimary else TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = RoseGoldPrimary,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = CardBorder
                ),
                modifier = Modifier.testTag(testTag)
            )
        }
    }
}
