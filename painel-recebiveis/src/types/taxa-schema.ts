import { z } from 'zod';

export const taxaFormSchema = z.object({
  moedaOrigem: z
    .string()
    .min(3, 'Use exatamente 3 caracteres (Ex: USD)')
    .max(3, 'Use exatamente 3 caracteres (Ex: USD)')
    .transform((val) => val.toUpperCase()),
  
  moedaDestino: z
    .string()
    .min(3, 'Use exatamente 3 caracteres (Ex: BRL)')
    .max(3, 'Use exatamente 3 caracteres (Ex: BRL)')
    .transform((val) => val.toUpperCase()),
  
  fatorConversao: z
    .number({ message: 'Informe um fator numérico válido' })
    .positive('O fator de conversão deve ser maior que zero'),
});

export type TaxaFormValues = z.infer<typeof taxaFormSchema>;
