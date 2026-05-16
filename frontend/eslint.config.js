import js from '@eslint/js'
import pluginVue from 'eslint-plugin-vue'
import * as tseslint from 'typescript-eslint'
import skipPrettier from 'eslint-config-prettier'
import globals from 'globals'

export default tseslint.config(
  {
    ignores: ['dist/**', 'node_modules/**', 'public/**', 'cypress/videos/**', 'cypress/screenshots/**']
  },
  js.configs.recommended,
  ...tseslint.configs.recommended,
  ...pluginVue.configs['flat/recommended'],
  skipPrettier,
  {
    files: ['**/*.{ts,vue,js}'],
    languageOptions: {
      parserOptions: {
        parser: tseslint.parser,
        extraFileExtensions: ['.vue']
      },
      globals: {
        ...globals.browser,
        ...globals.node,
        ...globals.jest,
        // Add Cypress globals manually
        cy: 'readonly',
        Cypress: 'readonly',
        describe: 'readonly',
        it: 'readonly',
        expect: 'readonly',
        beforeEach: 'readonly',
        afterEach: 'readonly'
      }
    },
    rules: {
      'vue/multi-word-component-names': 'off',
      '@typescript-eslint/no-explicit-any': 'off',
      'no-unused-vars': 'off', // Turn off base rule as TS handles it
      '@typescript-eslint/no-unused-vars': ['warn'],
      'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off'
    }
  }
)
