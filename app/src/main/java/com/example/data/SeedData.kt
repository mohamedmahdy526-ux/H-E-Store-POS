package com.example.data

object SeedData {
    val initialProducts = listOf(
        // عيون (Eyes)
        ProductEntity(
            name = "ماسكارا إيسنس آي لاف إكستريم السوداء",
            barcode = "4250338487515",
            category = "عيون",
            sellPrice = 185.0,
            costPrice = 130.0,
            stockQuantity = 18,
            minStockAlert = 3,
            description = "ماسكارا تكثيف الرموش المشهورة من إيسنس"
        ),
        ProductEntity(
            name = "ماسكارا إيسنس الوردية كيرل أند فوليوم",
            barcode = "4250338487522",
            category = "عيون",
            sellPrice = 185.0,
            costPrice = 130.0,
            stockQuantity = 2, // Low stock test
            minStockAlert = 3,
            description = "ماسكارا تطويل وتكثيف بفرشاة منحنية"
        ),
        ProductEntity(
            name = "ماسكارا ميبيلين سكاي هاي الأصلية",
            barcode = "3600531599720",
            category = "عيون",
            sellPrice = 420.0,
            costPrice = 310.0,
            stockQuantity = 8,
            minStockAlert = 3,
            description = "ماسكارا مايبيلين لاش سينسيشونال سكاي هاي"
        ),
        ProductEntity(
            name = "كحل ريميل لندن سكاندال آيز ضد الماء - أسود",
            barcode = "3607342541123",
            category = "عيون",
            sellPrice = 160.0,
            costPrice = 110.0,
            stockQuantity = 14,
            minStockAlert = 3,
            description = "قلم كحل كريمي ثابت ضد التلطخ"
        ),
        ProductEntity(
            name = "كحل ريميل لندن بلون بيج لتوسيع العين",
            barcode = "3607342541130",
            category = "عيون",
            sellPrice = 160.0,
            costPrice = 110.0,
            stockQuantity = 6,
            minStockAlert = 3,
            description = "قلم تحديد داخل الجفن باللون البيج النيود"
        ),
        ProductEntity(
            name = "آيلاينر لوريال باريس سوبر لاينر بريفيكت سليم",
            barcode = "3600522059349",
            category = "عيون",
            sellPrice = 275.0,
            costPrice = 195.0,
            stockQuantity = 10,
            minStockAlert = 3,
            description = "قلم آيلاينر ريشة دقيقة جداً وثبات فائق"
        ),
        ProductEntity(
            name = "آيلاينر جل تشارلوت - أسود داكن كربوني",
            barcode = "5060542721990",
            category = "عيون",
            sellPrice = 190.0,
            costPrice = 135.0,
            stockQuantity = 1, // Low stock alert!
            minStockAlert = 3,
            description = "جل لاينر مع فرشاة تحديد احترافية"
        ),
        ProductEntity(
            name = "باليت ايشادو نود أوبسيشنز 9 ألوان",
            barcode = "6291106034111",
            category = "عيون",
            sellPrice = 350.0,
            costPrice = 240.0,
            stockQuantity = 5,
            minStockAlert = 3,
            description = "درجات ترابية دافئة ولامعة عالية الصبغية"
        ),
        ProductEntity(
            name = "مصحح حواجب بودرة وبروميد شيجلام 2 في 1",
            barcode = "6973414981100",
            category = "عيون",
            sellPrice = 210.0,
            costPrice = 145.0,
            stockQuantity = 9,
            minStockAlert = 3,
            description = "قلم حواجب مزدوج شعرة بشعرة"
        ),
        ProductEntity(
            name = "جل حواجب مسكارا إيسنس فيكس آند لاست الشفاف",
            barcode = "4251232223314",
            category = "عيون",
            sellPrice = 145.0,
            costPrice = 98.0,
            stockQuantity = 12,
            minStockAlert = 3,
            description = "مثبت حواجب مقاوم للماء يدوم 16 ساعة"
        ),

        // شفايف (Lips)
        ProductEntity(
            name = "روج كيكو ميلانو سمارت فيوجن رقم 407",
            barcode = "8015018040700",
            category = "شفايف",
            sellPrice = 290.0,
            costPrice = 210.0,
            stockQuantity = 11,
            minStockAlert = 3,
            description = "أحمر شفاه كيكو ناعم ومرطب غني باللون"
        ),
        ProductEntity(
            name = "روج كيكو ميلانو سمارت فيوجن رقم 414",
            barcode = "8015018041400",
            category = "شفايف",
            sellPrice = 290.0,
            costPrice = 210.0,
            stockQuantity = 7,
            minStockAlert = 3,
            description = "روج كيكو أحمر كلاسيكي ساحر"
        ),
        ProductEntity(
            name = "روج ميبيلين سوبر ستاي فينيل إنك 35 تشيكي",
            barcode = "3600531652432",
            category = "شفايف",
            sellPrice = 450.0,
            costPrice = 340.0,
            stockQuantity = 4,
            minStockAlert = 3,
            description = "أحمر شفاه لامع وثابت يدوم 16 ساعة"
        ),
        ProductEntity(
            name = "روج ميبيلين سوبر ستاي مات إنك 65 سيدوكتريس",
            barcode = "3600531411138",
            category = "شفايف",
            sellPrice = 390.0,
            costPrice = 290.0,
            stockQuantity = 2, // Low stock!
            minStockAlert = 3,
            description = "روج مط نود هادئ يدوم طوال اليوم"
        ),
        ProductEntity(
            name = "ملمع شفاه فنتي بيوتي جلو بومب فنتي جلو",
            barcode = "8400266400012",
            category = "شفايف",
            sellPrice = 550.0,
            costPrice = 420.0,
            stockQuantity = 6,
            minStockAlert = 3,
            description = "جلوس مكثف ولامع مع زبدة الشيا"
        ),
        ProductEntity(
            name = "تينت شفايف وخدود بنفت بينيتنت الأصلي",
            barcode = "602004075112",
            category = "شفايف",
            sellPrice = 480.0,
            costPrice = 360.0,
            stockQuantity = 5,
            minStockAlert = 3,
            description = "تينت بلون الورد الطبيعي للشفاه والخدود"
        ),
        ProductEntity(
            name = "تينت شفايف كوري بلون الفراولة المنعشة",
            barcode = "8809516801211",
            category = "شفايف",
            sellPrice = 135.0,
            costPrice = 90.0,
            stockQuantity = 15,
            minStockAlert = 3,
            description = "مورد شفايف مائي خفيف وثابت"
        ),
        ProductEntity(
            name = "مرطب شفاه لابيلو بنكهة الكرز واللمعان الأحمر",
            barcode = "4005808365214",
            category = "شفايف",
            sellPrice = 85.0,
            costPrice = 60.0,
            stockQuantity = 24,
            minStockAlert = 3,
            description = "زبدة كاكاو لابيلو الشهيرة بترطيب 24 ساعة"
        ),
        ProductEntity(
            name = "محدد شفايف فلورمار درجة 201 نود وردي",
            barcode = "8690604122010",
            category = "شفايف",
            sellPrice = 95.0,
            costPrice = 65.0,
            stockQuantity = 18,
            minStockAlert = 3,
            description = "قلم محدد شفاه مقاوم للماء ناعم وسلس"
        ),

        // بشرة (Skin & Makeup Base)
        ProductEntity(
            name = "كريم أساس ميبيلين فيت مي مات درجة 120",
            barcode = "3600531072919",
            category = "بشرة",
            sellPrice = 360.0,
            costPrice = 270.0,
            stockQuantity = 8,
            minStockAlert = 3,
            description = "فاونديشن فيت مي للبشرة العادية والمختلطة"
        ),
        ProductEntity(
            name = "كريم أساس ميبيلين فيت مي مات درجة 128",
            barcode = "3600531072926",
            category = "بشرة",
            sellPrice = 360.0,
            costPrice = 270.0,
            stockQuantity = 6,
            minStockAlert = 3,
            description = "فاونديشن فيت مي درجة وورم نيود للبشرة الحنطية"
        ),
        ProductEntity(
            name = "كونسيلر ميبيلين إيريزر مضاد للشيخوخة درجة 01 لايت",
            barcode = "3600530733880",
            category = "بشرة",
            sellPrice = 320.0,
            costPrice = 235.0,
            stockQuantity = 9,
            minStockAlert = 3,
            description = "خافي عيوب إسفنجي للهالات والتغطية الفورية"
        ),
        ProductEntity(
            name = "كونسيلر فيت مي ميبيلين درجة 15 فير",
            barcode = "3600530751938",
            category = "بشرة",
            sellPrice = 250.0,
            costPrice = 180.0,
            stockQuantity = 12,
            minStockAlert = 3,
            description = "كونسيلر خفيف بتغطية طبيعية متجانسة"
        ),
        ProductEntity(
            name = "بودرة لوس باودر شفافة لورا مرسييه 29 جم",
            barcode = "736150102144",
            category = "بشرة",
            sellPrice = 650.0,
            costPrice = 490.0,
            stockQuantity = 3,
            minStockAlert = 3,
            description = "بودرة تثبيت حرة فائقة النعومة ومطفأة"
        ),
        ProductEntity(
            name = "بلاشر كريمي سائل شيجلام درجة لاف كيك",
            barcode = "6973414982216",
            category = "بشرة",
            sellPrice = 240.0,
            costPrice = 170.0,
            stockQuantity = 14,
            minStockAlert = 3,
            description = "أحمر خدود سائل وردي ناعم مع إسفنجة مدمجة"
        ),
        ProductEntity(
            name = "سبراي مثبت مكياج إربن ديكاي أول نايتر 118 مل",
            barcode = "3605970371442",
            category = "بشرة",
            sellPrice = 580.0,
            costPrice = 430.0,
            stockQuantity = 2, // Low stock!
            minStockAlert = 3,
            description = "مثبت مكياج يدوم 16 ساعة ضد الحرارة والرطوبة"
        ),
        ProductEntity(
            name = "بي بي كريم غارنييه مع واقي شمس SPF 50",
            barcode = "3600542385114",
            category = "بشرة",
            sellPrice = 195.0,
            costPrice = 140.0,
            stockQuantity = 11,
            minStockAlert = 3,
            description = "تغطية موحدة مع حماية يومية من الشمس"
        ),

        // عناية (Skincare & Bodycare)
        ProductEntity(
            name = "سيروم الهيالورونيك لاروش بوزيه بـ 5 حجم 30 مل",
            barcode = "3337875583626",
            category = "عناية",
            sellPrice = 750.0,
            costPrice = 560.0,
            stockQuantity = 5,
            minStockAlert = 3,
            description = "سيروم مكثف لترطيب البشرة واستعادة مرونتها"
        ),
        ProductEntity(
            name = "غسول سيرافيه للبشرة العادية إلى الجافة 236 مل",
            barcode = "3337875597180",
            category = "عناية",
            sellPrice = 410.0,
            costPrice = 310.0,
            stockQuantity = 7,
            minStockAlert = 3,
            description = "غسول مرطب بالسيراميدات وحمض الهيالورونيك"
        ),
        ProductEntity(
            name = "غسول سيرافيه الرغوي للبشرة الدهنية 236 مل",
            barcode = "3337875597210",
            category = "عناية",
            sellPrice = 420.0,
            costPrice = 315.0,
            stockQuantity = 8,
            minStockAlert = 3,
            description = "منظف عميق للمسام يتحكم في إفراز الدهون"
        ),
        ProductEntity(
            name = "واقي شمس بيوديرما فوتوديرم ماكس SPF 50+",
            barcode = "3401347869719",
            category = "عناية",
            sellPrice = 520.0,
            costPrice = 390.0,
            stockQuantity = 4,
            minStockAlert = 3,
            description = "سائل حماية فائق من أشعة الشمس بلمسة جافة"
        ),
        ProductEntity(
            name = "كريم مرطب بيبانثين الأزرق للوجه واليدين 30 جم",
            barcode = "6221087010211",
            category = "عناية",
            sellPrice = 120.0,
            costPrice = 90.0,
            stockQuantity = 22,
            minStockAlert = 3,
            description = "مرطب طبي ملطف بخلاصة الديكسبانثينول"
        ),
        ProductEntity(
            name = "سيروم نياسيناميد 10% والزنك ذا أورديناري 30 مل",
            barcode = "769915190312",
            category = "عناية",
            sellPrice = 380.0,
            costPrice = 280.0,
            stockQuantity = 6,
            minStockAlert = 3,
            description = "لتقليل التصبغات وآثار الحبوب وتضييق المسام"
        ),
        ProductEntity(
            name = "ماء ميسيلار غارنييه الوردي مزيل المكياج 400 مل",
            barcode = "3600541358485",
            category = "عناية",
            sellPrice = 165.0,
            costPrice = 120.0,
            stockQuantity = 16,
            minStockAlert = 3,
            description = "ينظف ويزيل كل أنواع المكياج بدون فرك"
        ),
        ProductEntity(
            name = "كريم كولاجين ديسار لنضارة وشد البشرة",
            barcode = "6932511218902",
            category = "عناية",
            sellPrice = 150.0,
            costPrice = 100.0,
            stockQuantity = 10,
            minStockAlert = 3,
            description = "كريم ترطيب عميق بالكولاجين البحري"
        ),

        // إكسسوارات (Accessories)
        ProductEntity(
            name = "إسفنجة دمج بيوتي بلندر الأصلية الماسية",
            barcode = "897321004122",
            category = "إكسسوارات",
            sellPrice = 75.0,
            costPrice = 45.0,
            stockQuantity = 25,
            minStockAlert = 3,
            description = "إسفنجة ناعمة لتوزيع كريم الأساس بدون خطوط"
        ),
        ProductEntity(
            name = "طقم فرش مكياج بي اتش كوزمتكس 10 قطع مع جراب",
            barcode = "849953008912",
            category = "إكسسوارات",
            sellPrice = 380.0,
            costPrice = 260.0,
            stockQuantity = 4,
            minStockAlert = 3,
            description = "مجموعة فرش احترافية للوجه والعيون بألياف ناعمة"
        ),
        ProductEntity(
            name = "مكبس رموش معدني احترافي مع قطع سيليكون غيار",
            barcode = "6921389410118",
            category = "إكسسوارات",
            sellPrice = 65.0,
            costPrice = 40.0,
            stockQuantity = 15,
            minStockAlert = 3,
            description = "مكبس عكف الرموش بدون تكسير"
        ),
        ProductEntity(
            name = "بكرات مساج الوجه روز كوارتز الحجر الطبيعي",
            barcode = "6942189201991",
            category = "إكسسوارات",
            sellPrice = 140.0,
            costPrice = 90.0,
            stockQuantity = 8,
            minStockAlert = 3,
            description = "رولر مع جواتشا للتصريف اللمفاوي وشد الوجه"
        ),
        ProductEntity(
            name = "توك شعر ستان حريري فاخر طقم 3 قطع",
            barcode = "6951238491001",
            category = "إكسسوارات",
            sellPrice = 45.0,
            costPrice = 25.0,
            stockQuantity = 30,
            minStockAlert = 3,
            description = "بندانة وتوك ستان تحمي الشعر من التقصف"
        ),
        ProductEntity(
            name = "مبرد أظافر كريستال زجاجي كوري",
            barcode = "8809182390112",
            category = "إكسسوارات",
            sellPrice = 50.0,
            costPrice = 28.0,
            stockQuantity = 20,
            minStockAlert = 3,
            description = "مبرد تلميع وتحديد الأظافر يدوم طويلاً"
        ),

        // عطور (Perfumes & Mists)
        ProductEntity(
            name = "عطر يارا لطافة الوردي الشهير 100 مل",
            barcode = "6291107458289",
            category = "عطور",
            sellPrice = 580.0,
            costPrice = 440.0,
            stockQuantity = 7,
            minStockAlert = 3,
            description = "عطر أنثوي جذاب بمزيج الفواكه والزهور والفانيليا"
        ),
        ProductEntity(
            name = "معطر جسم باث آند بودي وركس إنتو ذا نايت 236 مل",
            barcode = "667554901238",
            category = "عطور",
            sellPrice = 390.0,
            costPrice = 295.0,
            stockQuantity = 5,
            minStockAlert = 3,
            description = "بادي ميست فخم برائحة العنبر والتوت الداكن"
        ),
        ProductEntity(
            name = "معطر جسم باث آند بودي جينجهام الأزرق 236 مل",
            barcode = "667554901245",
            category = "عطور",
            sellPrice = 390.0,
            costPrice = 295.0,
            stockQuantity = 3,
            minStockAlert = 3,
            description = "عطر منعش بمزيج الزهور الزرقاء والخوخ الحلو"
        ),
        ProductEntity(
            name = "تولة مسك الطهارة الأصلي الفاخر 12 مل",
            barcode = "6281034011234",
            category = "عطور",
            sellPrice = 85.0,
            costPrice = 45.0,
            stockQuantity = 28,
            minStockAlert = 3,
            description = "مسك أبيض كريمي فاخر وثبات يدوم لأيام"
        ),
        ProductEntity(
            name = "عطر شعر وجسم مسك البودرة اللطيف 50 مل",
            barcode = "6281034011241",
            category = "عطور",
            sellPrice = 160.0,
            costPrice = 105.0,
            stockQuantity = 11,
            minStockAlert = 3,
            description = "رائحة النظافة البودرية المنعشة"
        ),

        // أطفال (Kids & Baby)
        ProductEntity(
            name = "شامبو بندولين للأطفال بزيت الأرجان 250 مل",
            barcode = "6224008123019",
            category = "أطفال",
            sellPrice = 130.0,
            costPrice = 95.0,
            stockQuantity = 12,
            minStockAlert = 3,
            description = "خالٍ من السلفات والبارابين بتركيبة لا دموع بعد اليوم"
        ),
        ProductEntity(
            name = "زيت جونسون للأطفال بالصبار المرطب 200 مل",
            barcode = "3574660309112",
            category = "أطفال",
            sellPrice = 95.0,
            costPrice = 70.0,
            stockQuantity = 16,
            minStockAlert = 3,
            description = "يحبس الرطوبة بنسبة تصل إلى 10 أضعاف"
        ),
        ProductEntity(
            name = "كريم سانوسان لمنطقة الحفاض للأطفال 100 مل",
            barcode = "4004503021109",
            category = "أطفال",
            sellPrice = 185.0,
            costPrice = 135.0,
            stockQuantity = 6,
            minStockAlert = 3,
            description = "حماية من الالتهابات بأكسيد الزنك وزيت الزيتون"
        ),
        ProductEntity(
            name = "كولونيا شيكو للأطفال برائحة البيبي بودر 100 مل",
            barcode = "8058664082110",
            category = "أطفال",
            sellPrice = 240.0,
            costPrice = 175.0,
            stockQuantity = 4,
            minStockAlert = 3,
            description = "عطر ناعم ومنعش خالٍ من الكحول للأطفال"
        ),
        ProductEntity(
            name = "طقم فرشاة ومشط شعر أطفال ناعم للرضع",
            barcode = "6921389410330",
            category = "أطفال",
            sellPrice = 55.0,
            costPrice = 30.0,
            stockQuantity = 14,
            minStockAlert = 3,
            description = "شعيرات طبيعية فائقة النعومة على فروة الرأس"
        )
    )
}
