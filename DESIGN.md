---
name: Artisanal Hearth
colors:
  surface: '#fff8f5'
  surface-dim: '#f6d4b6'
  surface-bright: '#fff8f5'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#fff1e7'
  surface-container: '#ffeada'
  surface-container-high: '#ffe3cc'
  surface-container-highest: '#fedcbe'
  on-surface: '#291806'
  on-surface-variant: '#524343'
  inverse-surface: '#402c18'
  inverse-on-surface: '#ffeee0'
  outline: '#847373'
  outline-variant: '#d6c2c1'
  surface-tint: '#855050'
  primary: '#855050'
  on-primary: '#ffffff'
  primary-container: '#eba8a8'
  on-primary-container: '#6d3b3c'
  inverse-primary: '#fab5b5'
  secondary: '#446648'
  on-secondary: '#ffffff'
  secondary-container: '#c3eac3'
  on-secondary-container: '#496b4b'
  tertiary: '#60603e'
  on-tertiary: '#ffffff'
  tertiary-container: '#bcbb92'
  on-tertiary-container: '#4b4b2a'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#ffdad9'
  primary-fixed-dim: '#fab5b5'
  on-primary-fixed: '#350f11'
  on-primary-fixed-variant: '#69393a'
  secondary-fixed: '#c6ecc5'
  secondary-fixed-dim: '#aad0ab'
  on-secondary-fixed: '#012109'
  on-secondary-fixed-variant: '#2d4e31'
  tertiary-fixed: '#e6e5b9'
  tertiary-fixed-dim: '#cac99f'
  on-tertiary-fixed: '#1d1d03'
  on-tertiary-fixed-variant: '#484828'
  background: '#fff8f5'
  on-background: '#291806'
  surface-variant: '#fedcbe'
typography:
  display-lg:
    fontFamily: Literata
    fontSize: 40px
    fontWeight: '700'
    lineHeight: 48px
    letterSpacing: -0.02em
  display-lg-mobile:
    fontFamily: Literata
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Literata
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  title-lg:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 28px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.05em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 8px
  container-padding-mobile: 1rem
  container-padding-desktop: 2rem
  gutter: 1.5rem
  sidebar-width: 280px
---

## Brand & Style

The design system is crafted for a modern bakery management environment, balancing the precision required for inventory and sales tracking with the warmth of a boutique kitchen. The target audience includes bakery owners and managers who need an efficient, high-performance interface that doesn't feel clinical or cold.

The aesthetic direction is **Corporate Modern with a Soft-Tactile Twist**. It leverages a structured, card-based layout to maintain organizational clarity, but softens the edges with a pastel-inspired palette and high-quality editorial typography. The goal is to evoke a sense of "organized warmth"—reliable and data-driven, yet inviting and artisanal.

## Colors

The palette is rooted in culinary-inspired tones that provide an immediate industry-specific context.

- **Primary (Dusty Rose):** Used for primary actions, active states, and critical brand moments. It provides a soft but distinct focal point.
- **Secondary (Mint Green):** Reserved for positive status indicators, "Success" states, and growth-related data points.
- **Tertiary (Cream):** The primary surface color for the application background. It reduces eye strain compared to pure white and adds a "paper-like" artisanal quality.
- **Neutral (Chocolate Brown):** The core color for all typography and borders. This replaces standard blacks or grays to maintain warmth while ensuring AAA accessibility for data tables.

## Typography

This design system utilizes a dual-type approach to bridge the gap between "Bakery" and "Admin Dashboard."

- **Headlines (Literata):** A scholarly, warm serif used for page titles and major card headings. It provides the "artisanal" character and literary feel of a recipe book.
- **Interface & Data (Inter):** A systematic sans-serif chosen for its exceptional legibility in dense data tables and form fields. 
- **Hierarchy Rule:** Use the serif strictly for structural headers. All functional labels, numbers, and input text must use the sans-serif for professional clarity.

## Layout & Spacing

The design system employs a **Fixed-Fluid Hybrid Grid**. The sidebar navigation remains fixed at 280px, while the main content area utilizes a 12-column fluid grid.

- **Rhythm:** A strict 8px spacing scale ensures consistency across components.
- **Margins:** Desktop views utilize generous 32px (2rem) outer margins to prevent the interface from feeling cramped, reinforcing the "minimalist" brand value.
- **Data Density:** In data-heavy tables, vertical padding is reduced to 12px, while standard dashboard cards use 24px padding to allow the content to "breathe."

## Elevation & Depth

This design system avoids heavy drop shadows in favor of **Tonal Layering and Soft Outlines.**

- **Surface Levels:** The base background is the Tertiary (Cream) color. Cards and containers are set in pure White (#FFFFFF) to create a subtle lift.
- **Outlines:** Instead of shadows, use 1px solid borders in a low-opacity Chocolate Brown (10-15% opacity). This creates a "stationary" feel that mimics high-end packaging.
- **Active State:** Only the "Active" or "Hovered" cards should feature a very soft, diffused shadow (Blur: 20px, Y: 10, Spread: -5, Color: Chocolate Brown at 8% opacity).

## Shapes

The shape language is consistently **Rounded**. The 0.5rem (8px) corner radius strikes a balance between professional software and the soft, organic nature of baked goods. 

Buttons and selection chips should use the `rounded-xl` or "Pill" style to distinguish them from structural containers like cards and input fields.

## Components

- **Buttons:** Primary buttons use the Dusty Rose background with White text. Secondary buttons use a Chocolate Brown outline with no fill.
- **Data Tables:** High contrast is achieved by using Chocolate Brown text on alternating subtle cream row stripes. Headers should be in the Label-SM style (Inter, Bold, All-Caps).
- **Status Badges:** Use the Secondary (Mint Green) for "In Stock/Complete" and a muted variant of Dusty Rose for "Low Stock/Pending."
- **Input Fields:** Use a 1px Chocolate Brown border at 20% opacity. On focus, the border thickens to 2px and shifts to the Primary Dusty Rose color.
- **Sidebar:** The navigation background should be the Neutral Chocolate Brown with Cream text to provide a strong visual anchor for the dashboard.
- **Cards:** White background, 1px soft border, and Literata headlines for the title section.