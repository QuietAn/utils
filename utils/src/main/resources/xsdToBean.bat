
::xsd文件路径
@set xsdHome=E:\User\Desktop\sb\HXZG_DJ_00004
::包
@set package=com.xml.gt3.hxzg_dj_00004
::编码
@set encoding=UTF-8
::javabean生成路径
@set modelPath=%xsdHome%\model
::xsd1
@set reqXsdName=%xsdHome%\TaxMLBw_HXZG_DJ_00004_Request_V1.0.xsd
::xsd2
@set rspXsdName=%xsdHome%\TaxMLBw_HXZG_DJ_00004_Response_V1.0.xsd

@if not exist %modelPath% (
    @md %modelPath% 
)

call xjc -d %modelPath% -p %package% -encoding %encoding% %reqXsdName%

if 1==%errorlevel% (
   @pause 
) else (
    call xjc -d %modelPath% -p %package% -encoding %encoding% %rspXsdName%

    @if 1==%errorlevel% (
        @pause 
    ) else (
        @start %modelPath%
    )
)