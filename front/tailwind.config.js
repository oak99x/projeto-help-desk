/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}"
  ],
  theme: {
    colors: {
      blue: {
        DEFAULT: '#2A669F',
        50: '#E4F7F8',
        100: '#CCEEF2',
        200: '#9CD7E5',
        300: '#6CB9D8',
        400: '#3B94CB',
        500: '#2A669F',
        600: '#234B83',
        700: '#1B3366',
        800: '#14204A',
        900: '#0C102E'
      },
      gray: {
        '50': '#ffffff',
        '100': '#fafafa',
        '200': '#f6f6f6',
        '300': '#f1f1f1',
        '400': '#ececec',
        '500': '#d9d9d9',
        '600': '#c7c7c7',
        '700': '#b4b4b4',
        '800': '#7a7a7a',
        '850': '#464646',
        '900': '#000000',
      },

      status: {
        'OPEN': '#3B82F6',
        'IN_PROGRESS': '#F59E0B',
        'PAUSED': '#6B7280',
        'CONCLUDED': '#10B981'
      }
    },
    fontFamily: {
      helvetica: ['Helvetica', 'sans-serif'],
      sans: ['Graphik', 'sans-serif'],
      serif: ['Merriweather', 'serif'],
    },
    extend: {},
  },
  plugins: [
    require('@tailwindcss/typography'),
    require("daisyui")],

  daisyui: {
    themes: [
      {
        mytheme: {
          "primary": "#14204A",
          "secondary": "#1967D2",
          "accent": "#D9D9D9",
          //"neutral": "#000000000",
          "warning": "#6CB9D8",
          "base-100": "#ffffff",
        },
      },
      "dark",
      "cupcake",
    ],
    darkTheme: "dark",
    base: true,
    styled: true,
    utils: true,

  },


}

