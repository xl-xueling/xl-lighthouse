export const getEchartsErrorOption = (theme,message,chartHeight = null) => {
    return {
        title: {
            text: message,
            x: 'center',
            top:chartHeight == null ? 80 : (chartHeight - 32)/2,
            textStyle: {
                color: theme == 'light' ? '#000' : '#fff',
                fontSize: 12,
                lineWidth: 2,
                lineHeight:20,
                fontColor:'black',
                fontFamily : 'sans-serif',
                fontWeight: 'normal',
            }
        }
    };
}

export const getEchartsLoadingOption = (theme) => {
    return {
        animation: false,
        icon: 'none',
        text: 'Loading...',
        color: '#c23531',
        showSpinner: true,
        spinnerRadius: 7,
        textColor: theme == 'light'?'#000' : '#fff',
        fontWeight: 'normal',
        lineWidth: 2,
        fontSize: 13,
        maskColor: theme == 'light'?'rgba(255, 255, 255, 1)' : 'rgba(41, 41, 44, 1)',
    };
}

export const getEchartsEmptyOption = (t:any,theme) => {
    return {
        title: {
            text: t['basic.chart.empty.warning'],
            x: 'center',
            top:'80px',
            textStyle: {
                color: theme == 'light' ? '#000' : '#fff',
                fontSize: 12,
                lineWidth: 2,
                lineHeight:30,
                fontColor:'black',
                fontFamily : 'sans-serif',
                fontWeight: 'normal',
            }
        }
    };
}